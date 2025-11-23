package com.example.avitotest.feature.auth.presentation.mvi.register

sealed interface RegisterEvents {
    data class ShowMessage(val message: String, val onRetryAction: () -> Unit) : RegisterEvents
    object RegisterSuccess : RegisterEvents
    object OnLogin: RegisterEvents
}
