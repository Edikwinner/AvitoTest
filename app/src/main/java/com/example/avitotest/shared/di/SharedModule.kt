package com.example.avitotest.shared.di

import com.example.avitotest.shared.data.repository.AuthRepositoryImpl
import com.example.avitotest.shared.data.repository.LocalFileRepositoryImpl
import com.example.avitotest.shared.data.repository.StorageRepositoryImpl
import com.example.avitotest.shared.data.services.FirebaseAuthService
import com.example.avitotest.shared.data.services.YandexCloudService
import com.example.avitotest.shared.domain.datasource.AuthDataSource
import com.example.avitotest.shared.domain.datasource.StorageDataSource
import com.example.avitotest.shared.domain.repository.AuthRepository
import com.example.avitotest.shared.domain.repository.LocalFileRepository
import com.example.avitotest.shared.domain.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        firebaseAuthService: FirebaseAuthService
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindStorageRepository(
        storageRepositoryImpl: StorageRepositoryImpl
    ): StorageRepository

    @Binds
    @Singleton
    abstract fun bindStorageDataSource(
        storageService: YandexCloudService
    ): StorageDataSource

    @Binds
    @Singleton
    abstract fun bindLocalFileRepository(
        localFileRepositoryImpl: LocalFileRepositoryImpl
    ): LocalFileRepository
}
