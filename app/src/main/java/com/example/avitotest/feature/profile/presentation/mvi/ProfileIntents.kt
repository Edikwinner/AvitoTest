package com.example.avitotest.feature.profile.presentation.mvi

import android.net.Uri

sealed interface ProfileIntents {
    object OnLogout: ProfileIntents
    object OnAvatarClick: ProfileIntents
    object OnEnterEditMode: ProfileIntents
    data class OnImageSelected(val imageUri: Uri): ProfileIntents
    object OnDisplayNameSave: ProfileIntents
    data class OnDisplayNameChanged(val displayName: String): ProfileIntents
}
