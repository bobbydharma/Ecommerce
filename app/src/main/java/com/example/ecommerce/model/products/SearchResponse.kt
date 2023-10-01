package com.example.ecommerce.model.products

data class SearchResponse(
    val code: Int,
    val message: String,
    val data: List<String>
)