package com.example.ecommerce.model.user

data class UserRequest(
    var email: String = "",
    var password: String = "",
    var firebaseToken: String = ""
)