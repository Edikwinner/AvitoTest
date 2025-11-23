package com.example.avitotest.feature.upload.di

import com.example.avitotest.feature.upload.data.repository.UploadRepositoryImpl
import com.example.avitotest.feature.upload.domain.repository.UploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UploadModule {

    @Binds
    abstract fun bindUploadRepository(
        uploadRepositoryImpl: UploadRepositoryImpl
    ): UploadRepository
}
