package com.example.ecommerce.model

data class UserRequest(
    var email: String = "",
    var password: String = "",
    var firebaseToken: String = ""
)