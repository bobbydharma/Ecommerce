package com.example.ecommerce.model.user

data class UserResponse(
    var code: Int,
    var message: String,
    var data: DataResponse
)

data class DataResponse(
    var accessToken: String,
    var refreshToken: String,
    var expiresAt: Int
)

