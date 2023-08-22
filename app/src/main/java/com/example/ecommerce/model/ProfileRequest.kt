package com.example.ecommerce.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class ProfileRequest (
    val userName: MultipartBody.Part,
    val userImage: MultipartBody.Part
)
