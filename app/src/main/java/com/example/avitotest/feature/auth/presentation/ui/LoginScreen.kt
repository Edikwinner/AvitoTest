package com.example.avitotest.feature.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.avitotest.feature.auth.presentation.mvi.login.LoginEvents
import com.example.avitotest.feature.auth.presentation.mvi.login.LoginIntents
import com.example.avitotest.feature.auth.presentation.mvi.login.LoginState
import com.example.avitotest.feature.auth.presentation.mvi.login.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onRegister: () -> Unit = {},
    onAuthSuccess: () -> Unit = {},
    onShowSnackBar: (String, () -> Unit) -> Unit = { _, _ -> }
) {
    val state by loginViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        loginViewModel.event.collect { event ->
            when (event) {
                is LoginEvents.ShowMessage -> onShowSnackBar(event.message, event.onRetryAction)
                is LoginEvents.LoginSuccess -> onAuthSuccess()
                is LoginEvents.OnRegister -> onRegister()
            }
        }
    }

    val snackBarHostState = remember { SnackbarHostState() }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordFieldFocusRequester = remember { FocusRequester() }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Вход в аккаунт") },
            actions = {
                TextButton(onClick = { loginViewModel.handleIntent(LoginIntents.OnRegisterClicked) }) {
                    Text("Регистрация")
                }
            }
        )
    }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = {
                    loginViewModel.handleIntent(LoginIntents.OnEmailChanged(it.trim()))
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFieldFocusRequester.requestFocus()
                    }),
                isError = state.emailError != null,
                supportingText = {
                    val error = state.emailError
                    if (error != null) {
                        Text(text = error)
                    }
                },
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    loginViewModel.handleIntent(LoginIntents.OnPasswordChanged(it))
                },
                label = { Text("Пароль") },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { loginViewModel.handleIntent(LoginIntents.OnPasswordVisibilityToggle) }) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (state.isPasswordVisible) "Скрыть пароль" else "Показать пароль"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFieldFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        loginViewModel.handleIntent(LoginIntents.OnLoginClicked)
                    }),
                isError = state.passwordError != null,
                supportingText = {
                    val error = state.passwordError
                    if (error != null) {
                        Text(text = error)
                    }
                },
            )

            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    loginViewModel.handleIntent(LoginIntents.OnLoginClicked)
                },
                enabled = state.state !is LoginState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.state is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), color = Color.Red, strokeWidth = 2.dp
                    )
                } else {
                    Text("Войти")
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}