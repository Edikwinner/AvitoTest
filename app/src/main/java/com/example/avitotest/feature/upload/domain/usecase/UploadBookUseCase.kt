package com.example.avitotest.feature.upload.domain.usecase

import android.app.Application
import android.net.Uri
import com.example.avitotest.feature.upload.domain.model.Book
import com.example.avitotest.feature.upload.domain.model.UploadResult
import com.example.avitotest.feature.upload.domain.repository.UploadRepository
import com.example.avitotest.shared.domain.datasource.AuthDataSource
import com.example.avitotest.shared.domain.repository.LocalFileRepository
import com.example.avitotest.shared.domain.repository.StorageRepository
import com.example.avitotest.shared.utils.getFileName
import com.example.avitotest.shared.utils.isFileSupported
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UploadBookUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val uploadRepository: UploadRepository,
    private val authDataSource: AuthDataSource,
    private val application: Application,
    private val localFileRepository: LocalFileRepository
) {

    operator fun invoke(
        title: String,
        author: String,
        fileUriString: String
    ): Flow<Result<UploadResult>> = flow {
        if (title.isBlank() || author.isBlank() || fileUriString.isBlank()) {
            emit(Result.failure(IllegalArgumentException("Title, author, and file cannot be empty")))
            return@flow
        }

        if (!Uri.parse(fileUriString).isFileSupported(application)) {
            emit(Result.failure(IllegalArgumentException("Unsupported file type. Only .txt, .epub, .pdf are allowed.")))
            return@flow
        }

        val userId = authDataSource.getCurrentUserId()
            ?: run { emit(Result.failure(Exception("User not authenticated"))); return@flow }

        emit(Result.success(UploadResult.InProgress(0.2f)))

        val localSaveFile = localFileRepository.saveFileLocally(
            uriString = fileUriString,
            fileName = Uri.parse(fileUriString).getFileName(application)
        )
        localSaveFile
            .onSuccess {
                val uploadFileResult = storageRepository.uploadFile(fileUriString)
                emit(Result.success(UploadResult.InProgress(0.4f)))

                uploadFileResult
                    .onSuccess {
                        val fileUrl = uploadFileResult.getOrThrow()
                        val book = Book(title, author, fileUrl, userId)

                        emit(Result.success(UploadResult.InProgress(0.7f)))

                        val addMetadataResult = uploadRepository.addBookMetadata(book)

                        if (addMetadataResult.isSuccess) {
                            emit(Result.success(UploadResult.Success(book)))
                        } else {
                            emit(
                                Result.failure(
                                    addMetadataResult.exceptionOrNull()
                                        ?: Exception("Failed to add book metadata")
                                )
                            )
                        }
                    }
                    .onFailure { e ->
                        emit(Result.failure(e))
                    }
            }
            .onFailure { e ->
                emit(Result.failure(e))
            }
    }
}
