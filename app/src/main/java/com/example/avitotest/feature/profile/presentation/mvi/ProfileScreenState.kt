package com.example.avitotest.feature.profile.presentation.mvi

import com.example.avitotest.feature.profile.domain.model.ProfileData

data class ProfileScreenState(
    val profileData: ProfileData? = null,
    val profileState: ProfileState = ProfileState.Idle,
    val isPhotoUploading: Boolean = false,
    val isDisplayNameUploading: Boolean = false,
    val isEditMode: Boolean = false
)

sealed interface ProfileState{
    object Idle : ProfileState
    object Loading : ProfileState
    data class Error(val message: String, val retryAction: () -> Unit) : ProfileState
}