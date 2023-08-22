package com.example.ecommerce.model

import retrofit2.http.Part
import java.io.File

data class ProfileRequest (
    @Part var userName: String,
    var userImage: File
)
