package com.example.avitotest.shared.domain.datasource

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface AuthDataSource{
    fun getCurrentUser(): FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun register(email: String, password: String): Result<FirebaseUser>
    suspend fun updateUserDisplayName(newName: String): Result<Unit>
    suspend fun updateUserPhoto(photoURL: String): Result<Unit>
    fun logout()
    fun getCurrentUserId(): String?
}