package com.example.ecommerce.core.model.products

import androidx.annotation.Keep

@Keep
data class ProductsResponse(
    val code: Int,
    val message: String,
    val data: Data
)

@Keep
data class Data(
    val itemsPerPage: Int,
    val currentItemCount: Int,
    val pageIndex: Int,
    val totalPages: Int,
    val items: List<Items>
)

@Keep
data class Items(
    val productId: String,
    val productName: String,
    val productPrice: Int,
    val image: String,
    val brand: String,
    val store: String,
    val sale: Int,
    val productRating: Float
)