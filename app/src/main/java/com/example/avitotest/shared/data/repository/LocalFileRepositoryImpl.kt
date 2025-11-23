package com.example.avitotest.shared.data.repository

import android.content.Context
import android.net.Uri
import com.example.avitotest.shared.domain.repository.LocalFileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LocalFileRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LocalFileRepository {

    private val localFilesDir: File by lazy { File(context.filesDir, "book_downloads") }

    override suspend fun saveFileLocally(uriString: String, fileName: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            if (!localFilesDir.exists()) {
                localFilesDir.mkdirs()
            }

            val outputFile = File(localFilesDir, fileName)

            val uri = Uri.parse(uriString)
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IOException("Failed to open input stream for URI: $uriString")

            inputStream.use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllSavedFiles(): Result<List<File>> =
        withContext(Dispatchers.IO) {
            try {
                if (!localFilesDir.exists()) {
                    return@withContext Result.success(emptyList())
                }

                val files = localFilesDir.listFiles()?.toList() ?: emptyList()
                Result.success(files)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
