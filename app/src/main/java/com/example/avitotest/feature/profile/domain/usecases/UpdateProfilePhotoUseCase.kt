package com.example.avitotest.feature.profile.domain.usecases

import android.net.Uri
import com.example.avitotest.feature.profile.domain.repository.ProfileRepository
import com.example.avitotest.shared.domain.usecase.UploadFileUseCase
import jakarta.inject.Inject

class UpdateProfilePhotoUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val uploadFileUseCase: UploadFileUseCase
) {
    suspend operator fun invoke(imageUri: Uri): Result<String> {
        val uploadResult = uploadFileUseCase(imageUri.toString())

        return if (uploadResult.isSuccess) {
            val url = uploadResult.getOrThrow()
            profileRepository.updateUserPhoto(url)
            uploadResult
        } else {
            Result.failure(uploadResult.exceptionOrNull() ?: Exception("Failed to upload photo"))
        }
    }
}
