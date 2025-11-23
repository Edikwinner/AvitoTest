package com.example.avitotest.feature.auth.presentation.mvi.login

sealed interface LoginIntents {
    data class OnEmailChanged(val email: String): LoginIntents
    data class OnPasswordChanged(val password: String): LoginIntents
    object OnPasswordVisibilityToggle: LoginIntents
    object OnLoginClicked : LoginIntents
    object OnRegisterClicked : LoginIntents
}