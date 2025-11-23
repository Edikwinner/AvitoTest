package com.example.avitotest.feature.upload.presentation.mvi

import android.net.Uri

sealed interface UploadIntent {
    data class TitleChanged(val title: String) : UploadIntent
    data class AuthorChanged(val author: String) : UploadIntent
    data class FileSelected(val uri: Uri) : UploadIntent
    object SelectFile: UploadIntent
    object UploadClicked : UploadIntent
}
