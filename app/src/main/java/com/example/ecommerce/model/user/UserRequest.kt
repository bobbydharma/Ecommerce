package com.example.ecommerce.model.user

import androidx.annotation.Keep

@Keep
data class UserRequest(
    var email: String = "",
    var password: String = "",
    var firebaseToken: String = ""
)