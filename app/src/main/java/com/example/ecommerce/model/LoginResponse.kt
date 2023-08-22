package com.example.ecommerce.model

data class LoginResponse (
    var code : Int,
    var message : String,
    var data : DataLoginResponse
)

data class DataLoginResponse(
    var userName: String,
    var userImage: String,
    var accessToken : String,
    var refreshToken : String,
    var expiresAt : Int
)