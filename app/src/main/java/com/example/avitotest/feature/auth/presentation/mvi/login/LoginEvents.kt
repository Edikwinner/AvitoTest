package com.example.avitotest.feature.auth.presentation.mvi.login

sealed interface LoginEvents {
    data class ShowMessage(val message: String, val onRetryAction: () -> Unit) : LoginEvents
    object LoginSuccess : LoginEvents
    object OnRegister: LoginEvents
}
