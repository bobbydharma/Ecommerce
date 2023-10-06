package com.example.ecommerce.core.model.rating

import androidx.annotation.Keep

@Keep
data class RatingRequest(
    val invoiceId: String,
    val rating: Int?,
    val review: String?
)

@Keep
data class RatingResponse(
    val code: Int,
    val message: String
)