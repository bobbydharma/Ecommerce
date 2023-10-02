package com.example.ecommerce.model.products

import androidx.annotation.Keep

@Keep
data class SearchResponse(
    val code: Int,
    val message: String,
    val data: List<String>
)