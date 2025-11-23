package com.example.avitotest.shared.domain.datasource

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StorageDataSource {

    /**
     * Uploads a file with progress tracking.
     * @param uri The Uri of the file to upload.
     * @param key The unique key (path) for the file in storage.
     * @return A Flow emitting upload progress.
     */
    suspend fun uploadFile(uriString: String, key: String): Result<String>

    /**
     * Downloads a file from storage.
     * @param key The key of the file to download.
     * @return A Result containing the downloaded File, or an exception.
     */
    suspend fun downloadFile(key: String): Result<File>
}
