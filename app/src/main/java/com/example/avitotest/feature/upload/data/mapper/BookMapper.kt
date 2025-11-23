package com.example.avitotest.feature.upload.data.mapper

import com.example.avitotest.feature.upload.domain.model.Book
import com.google.firebase.firestore.DocumentSnapshot

fun Book.toMap(): Map<String, Any> {
    return mapOf(
        "title" to title,
        "author" to author,
        "fileUrl" to fileUrl,
        "userId" to userId
    )
}

fun DocumentSnapshot.toBook(): Book? {
    return try {
        Book(
            title = getString("title") ?: "",
            author = getString("author") ?: "",
            fileUrl = getString("fileUrl") ?: "",
            userId = getString("userId") ?: ""
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}