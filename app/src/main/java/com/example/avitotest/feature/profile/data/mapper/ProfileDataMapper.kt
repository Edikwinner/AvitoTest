package com.example.avitotest.feature.profile.data.mapper

import android.net.Uri
import com.example.avitotest.feature.profile.domain.model.ProfileData
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): ProfileData = ProfileData(
    id = this.uid,
    displayName = this.displayName,
    email = email,
    phoneNumber = this.phoneNumber,
    photoUri = this.photoUrl.toString()
)