package com.example.avitotest.feature.auth.domain.usecases

import com.example.avitotest.shared.domain.repository.AuthRepository
import jakarta.inject.Inject

class IsAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean =
        authRepository.isAuthenticated()
}