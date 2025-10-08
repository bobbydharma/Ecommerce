package com.example.ecommerce.ui.prelogin.login.core.model

data class LoginDomainModel (
    var userName: String,
    var userImage: String,
    var accessToken: String,
    var refreshToken: String,
    var expiresAt: Int
)