package com.example.ecommerce.model.products

data class ProductsResponse(
    val code: Int,
    val message: String,
    val data: Data
)

data class Data(
    val itemsPerPage: Int,
    val currentItemCount: Int,
    val pageIndex: Int,
    val totalPages: Int,
    val items: List<Items>
)

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