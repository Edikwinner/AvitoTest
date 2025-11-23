package com.example.avitotest.feature.auth.utils

fun String.validateEmail(): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.validatePassword(): Boolean =
    this.length >= 8