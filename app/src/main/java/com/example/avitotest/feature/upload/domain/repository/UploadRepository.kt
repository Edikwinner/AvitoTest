package com.example.avitotest.feature.upload.domain.repository

import com.example.avitotest.feature.upload.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface UploadRepository {

    suspend fun addBookMetadata(book: Book): Result<Unit>

    fun getUploadedBooks(userId: String): Flow<List<Book>>
}
