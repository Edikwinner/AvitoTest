package com.example.avitotest.shared.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.example.avitotest.shared.domain.datasource.AuthDataSource
import com.example.avitotest.shared.domain.datasource.StorageDataSource
import com.example.avitotest.shared.domain.repository.StorageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class StorageRepositoryImpl @Inject constructor(
    private val storageService: StorageDataSource,
    private val authDataSource: AuthDataSource,
    @param:ApplicationContext private val context: Context
): StorageRepository {
    override suspend fun uploadFile(uriString: String): Result<String> {

        val uri = uriString.toUri()

        val userId = authDataSource.getCurrentUserId()
            ?: throw IllegalStateException("User not authenticated")

        val timestamp = System.currentTimeMillis()

        val fileExtension = getFileExtension(uri)

        val key = "${userId}_${timestamp}.${fileExtension ?: "jpg"}"

        return storageService.uploadFile(uriString, key)
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
