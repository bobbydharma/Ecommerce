package com.example.ecommerce.core.model.products

import androidx.annotation.Keep

@Keep
data class ReviewProduct(
    val code: Int,
    val message: String,
    val data: List<DataReview>
)

@Keep
data class DataReview(
    val userName: String,
    val userImage: String,
    val userRating: Int,
    val userReview: String
)