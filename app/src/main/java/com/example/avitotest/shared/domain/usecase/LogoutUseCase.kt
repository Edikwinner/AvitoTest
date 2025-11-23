package com.example.avitotest.shared.domain.usecase

import com.example.avitotest.shared.domain.repository.AuthRepository
import jakarta.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() =
        authRepository.logout()
}