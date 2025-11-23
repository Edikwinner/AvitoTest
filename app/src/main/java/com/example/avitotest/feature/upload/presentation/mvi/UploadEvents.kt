package com.example.avitotest.feature.upload.presentation.mvi

interface UploadEvents {
    object OnChooseFileClicked: UploadEvents
    data class ShowMessage(
        val message: String,
        val onRetryAction: (() -> Unit)?
    ) : UploadEvents

}