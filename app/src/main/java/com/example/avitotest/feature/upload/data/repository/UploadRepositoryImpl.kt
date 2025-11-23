package com.example.avitotest.feature.upload.data.repository

import com.example.avitotest.feature.upload.data.mapper.toBook
import com.example.avitotest.feature.upload.data.mapper.toMap
import com.example.avitotest.feature.upload.domain.model.Book
import com.example.avitotest.feature.upload.domain.repository.UploadRepository
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class UploadRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UploadRepository {

    companion object {
        private const val BOOKS_COLLECTION = "books"
    }

    override suspend fun addBookMetadata(book: Book): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            firestore.collection(BOOKS_COLLECTION)
                .add(book.toMap())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUploadedBooks(userId: String): Flow<List<Book>> = callbackFlow {
        val subscription = firestore.collection(BOOKS_COLLECTION)
            .whereEqualTo("userId", userId) // Filter books by userId
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val books = snapshot.documents.mapNotNull { document ->
                        document.toBook()
                    }
                    trySend(books).isSuccess
                }
            }
        awaitClose { subscription.remove() }
    }
}
