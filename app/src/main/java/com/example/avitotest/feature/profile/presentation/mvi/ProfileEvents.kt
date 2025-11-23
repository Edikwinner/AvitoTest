package com.example.avitotest.feature.profile.presentation.mvi

sealed interface ProfileEvents {
    object OnLogout : ProfileEvents
    data class ShowMessage(
        val message: String,
        val onRetryAction: () -> Unit
    ) : ProfileEvents
    object OnImageSelected : ProfileEvents
}