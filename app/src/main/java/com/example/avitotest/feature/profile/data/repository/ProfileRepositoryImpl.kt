package com.example.avitotest.feature.profile.data.repository

import android.net.Uri
import com.example.avitotest.feature.profile.data.mapper.toDomain
import com.example.avitotest.feature.profile.domain.model.ProfileData
import com.example.avitotest.feature.profile.domain.repository.ProfileRepository
import com.example.avitotest.shared.domain.datasource.AuthDataSource
import com.google.firebase.auth.UserProfileChangeRequest
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : ProfileRepository {
    override fun getUser(): Result<ProfileData> {
        val user = authDataSource.getCurrentUser()
        return if (user != null) {
            Result.success(user.toDomain())
        } else {
            Result.failure(NoSuchElementException("User not found"))
        }
    }

    override suspend fun updateUserDisplayName(newDisplayName: String): Result<Unit> =
        authDataSource.updateUserDisplayName(newDisplayName)


    override suspend fun updateUserPhoto(url: String): Result<Unit> {
        return try {
            val user = authDataSource.getCurrentUser()
                ?: return Result.failure(Exception("Пользователь не авторизован"))

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(url))
                .build()

            user.updateProfile(profileUpdates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}