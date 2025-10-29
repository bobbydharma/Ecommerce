package com.example.ecommerce.core.model.user

import androidx.annotation.Keep

@Keep
data class ProfileResponse(
    val code: Int,
    val message: String,
    val data: DataProfilResponse
)

@Keep
data class DataProfilResponse(
    val userName: String?,
    val userImage: String?
)