package com.example.avitotest.feature.upload.presentation.mvi

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotest.feature.upload.domain.model.UploadResult
import com.example.avitotest.feature.upload.domain.usecase.UploadBookUseCase
import com.example.avitotest.shared.utils.getFileName
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val uploadBookUseCase: UploadBookUseCase,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(UploadScreenState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UploadEvents>()
    val event: SharedFlow<UploadEvents> = _event.asSharedFlow()

    fun handleIntent(intent: UploadIntent) {
        when (intent) {
            is UploadIntent.TitleChanged -> updateTitle(intent.title)
            is UploadIntent.AuthorChanged -> updateAuthor(intent.author)
            is UploadIntent.FileSelected -> onFileSelected(intent.uri)
            is UploadIntent.UploadClicked -> uploadBook()
            is UploadIntent.SelectFile -> selectFile()
        }
    }

    private fun updateTitle(title: String) {
        _state.update { it.copy(title = title) }
    }

    private fun updateAuthor(author: String) {
        _state.update { it.copy(author = author) }
    }


    private fun onFileSelected(uri: Uri) {
        val fileName = uri.getFileName(application)
        _state.update { it.copy(selectedFileUri = uri, selectedFileName = fileName) }
    }

    private fun uploadBook() {
        Log.i("TAG", "uploadBook: clicked")
        _state.update { it.copy(uploadState = UploadState.Uploading()) }

        viewModelScope.launch {
            uploadBookUseCase(
                title = _state.value.title,
                author = _state.value.author,
                fileUriString = _state.value.selectedFileUri.toString()
            )
                .catch { e ->
                    _state.update {
                        it.copy(
                            uploadState = UploadState.Error(
                                e.message ?: "Upload failed"
                            )
                        )
                    }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { uploadResult ->
                            when (uploadResult) {
                                is UploadResult.InProgress -> _state.update {
                                    it.copy(
                                        uploadState = UploadState.Uploading(
                                            uploadResult.percent
                                        )
                                    )
                                }

                                is UploadResult.Success -> {
                                    _state.update {
                                        it.copy(
                                            uploadState = UploadState.Idle
                                        )
                                    }
                                    _event.emit(
                                        UploadEvents.ShowMessage(
                                            "Книга успешно загружена",
                                            null
                                        )
                                    )

                                }
                            }

                        },
                        onFailure = { e ->
                            _state.update {
                                it.copy(
                                    uploadState = UploadState.Idle
                                )
                            }
                            _event.emit(
                                UploadEvents.ShowMessage(
                                    e.message ?: "Upload failed",
                                    null
                                )
                            )
                        }
                    )
                }
        }
    }

    private fun selectFile() {
        viewModelScope.launch {
            _event.emit(UploadEvents.OnChooseFileClicked)
        }
    }
}
