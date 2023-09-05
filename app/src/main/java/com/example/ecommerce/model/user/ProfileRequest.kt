package com.example.ecommerce.model.user

import okhttp3.MultipartBody

data class ProfileRequest(
    val userName: MultipartBody.Part,
    val userImage: MultipartBody.Part
)