package com.example.avitotest.shared.domain.usecase

import com.example.avitotest.shared.domain.repository.StorageRepository
import jakarta.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(uriString: String): Result<String> {
        return storageRepository.uploadFile(uriString)
    }
}
