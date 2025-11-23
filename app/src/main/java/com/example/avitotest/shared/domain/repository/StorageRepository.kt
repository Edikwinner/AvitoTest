package com.example.avitotest.shared.domain.repository

import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    /**
     * Uploads a file from the given content Uri string.
     * @param uriString The content Uri of the file to upload, as a String.
     * @return A Flow that emits upload progress updates.
     */
    suspend fun uploadFile(uriString: String): Result<String>
}
