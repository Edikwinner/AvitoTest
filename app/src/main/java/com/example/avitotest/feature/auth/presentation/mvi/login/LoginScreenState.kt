package com.example.avitotest.feature.auth.presentation.mvi.login

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val state: LoginState = LoginState.Idle
)

sealed interface LoginState {
    object Idle : LoginState

    object Loading : LoginState
}