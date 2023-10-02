package com.example.ecommerce.model.user

import androidx.annotation.Keep

@Keep
data class LoginResponse(
    var code: Int,
    var message: String,
    var data: DataLoginResponse
)

@Keep
data class DataLoginResponse(
    var userName: String,
    var userImage: String,
    var accessToken: String,
    var refreshToken: String,
    var expiresAt: Int
)