package com.example.avitotest.shared.data.services

import android.net.Uri
import androidx.core.net.toUri
import com.example.avitotest.shared.domain.datasource.AuthDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthDataSource {
    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user ?: throw Exception("User is null after sign-in"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user ?: throw Exception("User is null after sign-up"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? = getCurrentUser()?.uid

    override fun getCurrentUser() = firebaseAuth.currentUser

    override suspend fun updateUserPhoto(photoURL: String): Result<Unit> {
        val user = getCurrentUser()
        return if (user != null) {
            try {
                user.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setPhotoUri(photoURL.toUri())
                        .build()
                ).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("Пользователь не авторизован"))
        }
    }

    override suspend fun updateUserDisplayName(newName: String): Result<Unit> {
        val user = getCurrentUser()
        return if (user != null) {
            try {
                user.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build()
                ).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("Пользователь не авторизован"))
        }
    }
}