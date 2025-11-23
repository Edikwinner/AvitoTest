package com.example.avitotest.feature.auth.presentation.mvi.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotest.feature.auth.domain.usecases.LoginUseCase
import com.example.avitotest.feature.auth.utils.validateEmail
import com.example.avitotest.feature.auth.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginScreenState())
    val state: StateFlow<LoginScreenState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvents>()
    val event: SharedFlow<LoginEvents> = _event.asSharedFlow()

    fun handleIntent(intent: LoginIntents) {
        when (intent) {
            is LoginIntents.OnLoginClicked -> login()
            is LoginIntents.OnEmailChanged -> updateEmail(intent.email)
            is LoginIntents.OnPasswordChanged -> updatePassword(intent.password)
            is LoginIntents.OnPasswordVisibilityToggle -> togglePasswordVisibility()
            is LoginIntents.OnRegisterClicked -> onRegisterScreen()
        }
    }

    private fun login() {
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
            it.copy(state = LoginState.Loading)
        }

        viewModelScope.launch {
            loginUseCase(_state.value.email, _state.value.password)
                .onSuccess {
                    _state.update {
                        _event.emit(LoginEvents.LoginSuccess)
                        it.copy(state = LoginState.Idle)
                    }
                }
                .onFailure { e ->
                    _event.emit(
                        LoginEvents.ShowMessage(
                            e.message ?: "Unknown error"
                        ) {
                            handleIntent(LoginIntents.OnLoginClicked)
                        })
                    _state.update {
                        it.copy(state = LoginState.Idle)
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

    private fun onRegisterScreen() {
        viewModelScope.launch {
            _event.emit(LoginEvents.OnRegister)
        }
    }
}
