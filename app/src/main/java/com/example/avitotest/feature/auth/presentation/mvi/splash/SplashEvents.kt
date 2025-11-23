package com.example.avitotest.feature.auth.presentation.mvi.splash

sealed interface SplashEvents {
    object OnAuthSuccess : SplashEvents
    object OnAuthFailed : SplashEvents
}