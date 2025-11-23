package com.example.avitotest.feature.auth.presentation.mvi.register

sealed interface RegisterIntents {
    data class OnEmailChanged(val email: String): RegisterIntents
    data class OnPasswordChanged(val password: String): RegisterIntents
    object OnPasswordVisibilityToggle: RegisterIntents
    object OnRegisterClicked : RegisterIntents
    object OnLoginClicked : RegisterIntents
}