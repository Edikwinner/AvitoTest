package com.example.avitotest.core.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object LoginScreen : Screen(route = "login")
    object RegisterScreen : Screen(route = "register")

    object LoadedScreen : Screen(route = "loaded")
    object UploadScreen : Screen(route = "upload")
    object ReaderScreen : Screen("reader")
    object ProfileScreen : Screen("profile")
}