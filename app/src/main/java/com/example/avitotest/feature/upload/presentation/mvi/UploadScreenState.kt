package com.example.avitotest.feature.upload.presentation.mvi

import android.net.Uri

data class UploadScreenState(
    val title: String = "",
    val author: String = "",
    val selectedFileUri: Uri? = null,
    val selectedFileName: String = "",
    val uploadState: UploadState = UploadState.Idle
)

sealed interface UploadState {
    object Idle : UploadState

    data class Uploading(val progress: Float = 0.0f) : UploadState

    object Success : UploadState

    data class Error(val message: String) : UploadState
}
