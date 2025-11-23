package com.example.avitotest.shared.utils

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap

fun Uri.getFileName(application: Application): String {
    var fileName = "Unknown file"
    application.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = cursor.getString(nameIndex)
            }
        }
    }
    return fileName
}

fun Uri.getMimeType(application: Application): String {
    application.contentResolver.getType(this)?.let { type ->
        return type
    }

    val extension = MimeTypeMap.getFileExtensionFromUrl(toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
        ?: "application/octet-stream"
}

fun Uri.isFileSupported(application: Application): Boolean {
    val allowedExtensions = setOf("txt", "epub", "pdf")
    val allowedMimeTypes = setOf(
        "text/plain",            // txt
        "application/epub+zip",  // epub
        "application/pdf"        // pdf
    )

    val mimeType = getMimeType(application)
    if (mimeType !in allowedMimeTypes) return false

    val name = getFileName(application)
    val extension = name.substringAfterLast('.', "").lowercase()
    if (extension !in allowedExtensions) return false

    return true
}