package com.example.avitotest.feature.profile.domain.model

data class ProfileData(
    val id: String,
    val displayName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoUri: String?
)