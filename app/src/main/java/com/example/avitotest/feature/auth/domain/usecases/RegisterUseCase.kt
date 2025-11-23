package com.example.avitotest.feature.auth.domain.usecases

import com.example.avitotest.shared.domain.repository.AuthRepository
import jakarta.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        authRepository.register(email, password)
}