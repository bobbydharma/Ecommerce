package com.example.ecommerce.core.model.user

import androidx.annotation.Keep
import okhttp3.MultipartBody

@Keep
data class ProfileRequest(
    val userName: MultipartBody.Part,
    val userImage: MultipartBody.Part?
)