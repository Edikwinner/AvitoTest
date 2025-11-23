package com.example.avitotest.feature.profile.domain.usecases

import com.example.avitotest.feature.profile.domain.repository.ProfileRepository
import jakarta.inject.Inject

class GetProfileDataUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke() = profileRepository.getUser()
}