package com.example.ecommerce.core.model.user

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginResponse(
    var code: Int,
    var message: String,
    var data: DataLoginResponse
)

@Keep
data class DataLoginResponse(
    var userName: String?,
    var userImage: String?,
    var accessToken: String?,
    var refreshToken: String?,
    var expiresAt: Int?
)

@Keep
data class ErrorResponse(
    var code: Int,
    var message: String
)