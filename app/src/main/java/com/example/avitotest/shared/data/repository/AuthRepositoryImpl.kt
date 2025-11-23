package com.example.avitotest.shared.data.repository

import com.example.avitotest.shared.domain.datasource.AuthDataSource
import com.example.avitotest.shared.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return authDataSource.login(email, password).map {  }
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        return authDataSource.register(email, password).map {  }
    }

    override fun logout() {
        authDataSource.logout()
    }

    override fun isAuthenticated(): Boolean {
        return authDataSource.getCurrentUser() != null
    }

}