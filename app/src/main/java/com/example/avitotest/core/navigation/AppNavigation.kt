package com.example.avitotest.core.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avitotest.feature.auth.presentation.ui.LoginScreen
import com.example.avitotest.feature.auth.presentation.ui.RegisterScreen
import com.example.avitotest.feature.auth.presentation.ui.SplashScreen
import com.example.avitotest.feature.loaded.presentation.ui.LoadedScreen
import com.example.avitotest.feature.profile.presentation.ui.ProfileScreen
import com.example.avitotest.feature.reader.ReaderScreen
import com.example.avitotest.feature.upload.presentation.ui.UploadScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    fun showSnackBar(message: String, retryAction: (() -> Unit)?) {
        coroutineScope.launch {
            val result = snackBarHostState.showSnackbar(
                message = message,
                actionLabel = if(retryAction == null) null else "Повторить",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed && retryAction != null) {
                retryAction()
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.SplashScreen.route
        ) {
            composable(Screen.SplashScreen.route) {
                SplashScreen(
                    onAuthSuccess = {
                        navController.navigate(Screen.LoadedScreen.route) {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                        }
                    },
                    onAuthError = {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.LoginScreen.route) {
                LoginScreen(
                    onAuthSuccess = {
                        navController.navigate(Screen.LoadedScreen.route) {
                            popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        }
                    },
                    onRegister = {
                        navController.navigate(Screen.RegisterScreen.route)
                    },
                    onShowSnackBar = ::showSnackBar
                )
            }

            composable(Screen.RegisterScreen.route) {
                RegisterScreen(
                    onAuthSuccess = {
                        navController.navigate(Screen.LoadedScreen.route) {
                            popUpTo(Screen.RegisterScreen.route) { inclusive = true }
                        }
                    },
                    onLogin = {
                        navController.navigate(Screen.LoginScreen.route)
                    },
                    onShowSnackBar = ::showSnackBar
                )
            }

            composable(Screen.LoadedScreen.route) {
                MainNavHost(
                    showSnackBar = ::showSnackBar,
                    onLogout = {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.LoadedScreen.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainNavHost(
    showSnackBar: (String, (() -> Unit)?) -> Unit,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = { BottomBar(bottomNavController) },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.LoadedScreen.route,
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
            )
        ) {
            composable(Screen.LoadedScreen.route) { LoadedScreen(
            ) }
            composable(Screen.ProfileScreen.route) { ProfileScreen(
                onLogout = onLogout,
                showSnackBar = showSnackBar
            ) }
            composable(Screen.ReaderScreen.route) { ReaderScreen() }
            composable(Screen.UploadScreen.route) { UploadScreen(
                showSnackBar = showSnackBar
            ) }
        }
    }
}