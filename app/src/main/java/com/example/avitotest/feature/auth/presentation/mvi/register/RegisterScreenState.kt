package com.example.avitotest.feature.auth.presentation.mvi.register

data class RegisterScreenState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val state: RegisterState = RegisterState.Idle
)

sealed interface RegisterState {
    object Idle : RegisterState

    object Loading : RegisterState
}