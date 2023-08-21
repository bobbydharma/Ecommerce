package com.example.ecommerce.model

data class RegisterResponse (
    var code : Int,
    var message : String,
    var data : DataResponse
)

data class DataResponse(
    var accessToken : String,
    var refreshToken : String,
    var expiresAt : Int
)