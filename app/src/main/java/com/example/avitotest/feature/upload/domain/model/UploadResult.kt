package com.example.avitotest.feature.upload.domain.model

sealed interface UploadResult {
    data class InProgress(val percent: Float) : UploadResult
    data class Success(val book: Book) : UploadResult
}
