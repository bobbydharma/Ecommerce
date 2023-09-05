package com.example.ecommerce.model.products

data class ReviewProduct (
    val code: Int,
    val message: String,
    val data: List<DataReview>
)

data class DataReview (
    val userName: String,
    val userImage: String,
    val userRating: Int,
    val userReview: String
)