package com.example.avitotest.feature.auth.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.avitotest.R
import com.example.avitotest.feature.auth.presentation.mvi.splash.SplashEvents
import com.example.avitotest.feature.auth.presentation.mvi.splash.SplashScreenState
import com.example.avitotest.feature.auth.presentation.mvi.splash.SplashViewModel

@Composable
fun SplashScreen(
    onAuthSuccess: () -> Unit,
    onAuthError: () -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
       splashViewModel.event.collect { event ->
           when(event){
               SplashEvents.OnAuthFailed -> onAuthError()
               SplashEvents.OnAuthSuccess -> onAuthSuccess()
           }
       }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
        }
    }

}