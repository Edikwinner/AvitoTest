package com.example.avitotest.feature.loaded.presentation.mvi

import androidx.lifecycle.ViewModel
import com.example.avitotest.shared.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class LoadedViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    fun logout(){
        logoutUseCase()
    }
}