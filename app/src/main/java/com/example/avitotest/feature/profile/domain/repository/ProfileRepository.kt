package com.example.avitotest.feature.profile.domain.repository

import com.example.avitotest.feature.profile.domain.model.ProfileData

interface ProfileRepository {
    fun getUser(): Result<ProfileData>
    suspend fun updateUserDisplayName(newDisplayName: String): Result<Unit>
    suspend fun updateUserPhoto(url: String): Result<Unit>
}