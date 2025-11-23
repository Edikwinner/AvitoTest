package com.example.avitotest.shared.domain.repository

import java.io.File

interface LocalFileRepository {

    suspend fun saveFileLocally(uriString: String, fileName: String): Result<File>

    suspend fun getAllSavedFiles(): Result<List<File>>
}
