package com.example.avitotest.feature.auth.presentation.mvi.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotest.feature.auth.domain.usecases.IsAuthenticatedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : ViewModel() {

    private val _event = MutableSharedFlow<SplashEvents>(1)
    val event: SharedFlow<SplashEvents> = _event.asSharedFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            delay(500)
            if(isAuthenticatedUseCase()) {
                _event.emit(SplashEvents.OnAuthSuccess)
            } else {
                _event.emit(SplashEvents.OnAuthFailed)
            }
        }
    }
}