package com.example.avitotest.feature.profile.domain.usecases

import com.example.avitotest.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileDisplayNameUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(newDisplayName: String): Result<Unit> =
        profileRepository.updateUserDisplayName(newDisplayName)
}