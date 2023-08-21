package com.example.ecommerce.model

data class RegisterRequest (
    var email : String,
    var password : String,
    var firebaseToken : String = ""
)