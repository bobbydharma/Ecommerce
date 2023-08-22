package com.example.ecommerce.model

data class ProfileResponse (
    val code: Int,
    val message: String,
    val data: DataProfilResponse
)

data class DataProfilResponse(
    val userName: String,
    val userImage: String
)