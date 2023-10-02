package com.example.ecommerce.model.user

import androidx.annotation.Keep

@Keep
data class UserResponse(
    var code: Int,
    var message: String,
    var data: DataResponse
)

@Keep
data class DataResponse(
    var accessToken: String,
    var refreshToken: String,
    var expiresAt: Int
)

