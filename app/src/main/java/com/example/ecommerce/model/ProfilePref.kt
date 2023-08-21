package com.example.ecommerce.model

data class ProfilePref (
    var name: String = "",
    var image: String = "",
    var token: String = "",
    var refreshToken: String = "",
    var obCheck: Boolean = false
)