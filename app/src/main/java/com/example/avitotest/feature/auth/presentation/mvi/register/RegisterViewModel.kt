package com.example.avitotest.feature.auth.presentation.mvi.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotest.feature.auth.domain.usecases.RegisterUseCase
import com.example.avitotest.feature.auth.presentation.mvi.login.LoginEvents
import com.example.avitotest.feature.auth.presentation.mvi.login.LoginIntents
import com.example.avitotest.feature.auth.presentation.mvi.login.LoginState
import com.example.avitotest.feature.auth.utils.validateEmail
import com.example.avitotest.feature.auth.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
): ViewModel() {
    private val _state = MutableStateFlow<RegisterScreenState>(RegisterScreenState())
    val state: StateFlow<RegisterScreenState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<RegisterEvents>()
    val event: SharedFlow<RegisterEvents> = _event.asSharedFlow()

    fun handleIntent(intent: RegisterIntents) {
        when (intent) {
            is RegisterIntents.OnRegisterClicked -> register()
            is RegisterIntents.OnEmailChanged -> updateEmail(intent.email)
            is RegisterIntents.OnPasswordChanged -> updatePassword(intent.password)
            is RegisterIntents.OnPasswordVisibilityToggle -> togglePasswordVisibility()
            is RegisterIntents.OnLoginClicked -> onLoginScreen()
        }
    }

    private fun register() {

        val emailError =
            if (!_state.value.email.validateEmail()) "Введите корректный email" else null
        val passwordError =
            if (!_state.value.password.validatePassword()) "Пароль должен содержать минимум 8 символов" else null

        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }

        if (emailError != null || passwordError != null) return

        _state.update {
            it.copy(state = RegisterState.Loading)
        }

        viewModelScope.launch {
            registerUseCase(_state.value.email, _state.value.password)
                .onSuccess {
                    _state.update {
                        _event.emit(RegisterEvents.RegisterSuccess)
                        it.copy(state = RegisterState.Idle)
                    }
                }
                .onFailure { e ->
                    _event.emit(RegisterEvents.ShowMessage(
                        e.message ?: "Unknown error"){
                        handleIntent(RegisterIntents.OnRegisterClicked)
                    })
                    _state.update {
                        it.copy(state = RegisterState.Idle)
                    }
                }
        }
    }

    private fun updateEmail(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }

    private fun updatePassword(password: String) {
        _state.update {
           it.copy(password = password)
        }
    }

    private fun togglePasswordVisibility() {
        _state.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    private fun onLoginScreen() {
        viewModelScope.launch {
            _event.emit(RegisterEvents.OnLogin)
        }
    }
}