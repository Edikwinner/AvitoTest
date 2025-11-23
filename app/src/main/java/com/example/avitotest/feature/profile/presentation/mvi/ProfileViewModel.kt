package com.example.avitotest.feature.profile.presentation.mvi

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avitotest.feature.profile.domain.usecases.GetProfileDataUseCase
import com.example.avitotest.feature.profile.domain.usecases.UpdateProfileDisplayNameUseCase
import com.example.avitotest.feature.profile.domain.usecases.UpdateProfilePhotoUseCase
import com.example.avitotest.shared.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase,
    private val updateProfileDisplayNameUseCase: UpdateProfileDisplayNameUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState())
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvents>(1)
    val event: SharedFlow<ProfileEvents> = _event.asSharedFlow()

    init {
        loadProfile()
    }

    fun handleIntent(intent: ProfileIntents) {
        when (intent) {
            is ProfileIntents.OnAvatarClick -> onAvatarClick()
            is ProfileIntents.OnLogout -> logout()
            is ProfileIntents.OnImageSelected -> saveImage(intent.imageUri)
            is ProfileIntents.OnDisplayNameChanged -> updateDisplayName(intent.displayName)
            is ProfileIntents.OnDisplayNameSave -> saveDisplayName()
            is ProfileIntents.OnEnterEditMode -> onEnterEditMode()
        }
    }

    private fun onAvatarClick() {
        viewModelScope.launch {
            _event.emit(ProfileEvents.OnImageSelected)
        }
    }

    private fun logout() {
        logoutUseCase()
        viewModelScope.launch {
            _event.emit(ProfileEvents.OnLogout)
        }
    }

    private fun loadProfile() {
        _state.update {
            it.copy(
                profileState = ProfileState.Loading
            )
        }
        getProfileDataUseCase()
            .onSuccess { profileData ->
                _state.update {
                    it.copy(
                        profileData = profileData,
                        profileState = ProfileState.Idle
                    )
                }
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        profileState = ProfileState.Error(e.message ?: "Unknown error") {
                            loadProfile()
                        })
                }
            }
    }

    private fun saveImage(imageUri: Uri) {

        _state.update {
            it.copy(isPhotoUploading = true)
        }

        viewModelScope.launch {
            updateProfilePhotoUseCase(imageUri)
                .onSuccess { imageUrl ->
                    _state.update {
                        it.copy(
                            profileData = it.profileData?.copy(photoUri = imageUrl),
                            isPhotoUploading = false
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isPhotoUploading = false,
                            profileState = ProfileState.Error(e.message ?: "Unknown error") {
                                ProfileIntents.OnImageSelected(imageUri)
                            })
                    }
                }
        }
    }

    private fun saveDisplayName() {
        val newDisplayName = _state.value.profileData?.displayName
        if (newDisplayName.isNullOrEmpty()) {
            return
        }

        _state.update {
            it.copy(
                isDisplayNameUploading = true,
                isEditMode = false
            )
        }

        viewModelScope.launch {
            updateProfileDisplayNameUseCase(newDisplayName)
                .onSuccess {
                    _state.update {
                        it.copy(
                            profileData = it.profileData?.copy(displayName = newDisplayName),
                            isDisplayNameUploading = false,
                            isEditMode = false
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isDisplayNameUploading = false,
                            isEditMode = false
                        )
                    }
                    _event.emit(
                        ProfileEvents.ShowMessage(
                            e.message ?: "Unknown error"
                        ) { saveDisplayName() }
                    )
                }
        }
    }

    private fun onEnterEditMode() {
        _state.update {
            it.copy(isEditMode = true)
        }
    }

    private fun updateDisplayName(newDisplayName: String) {
        _state.update {
            it.copy(profileData = it.profileData?.copy(displayName = newDisplayName))
        }
    }
}