package com.example.ecommerce.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey
    val productId: String,
    val productName: String,
    var productPrice: Int,
    val image: String,
    val brand: String,
    val description: String,
    val store: String,
    val sale: Int,
    val stock: Int,
    val totalRating: Int,
    val totalReview: Int,
    val totalSatisfaction: Int,
    val productRating: Float,
    val varianName: String,
    val varianPrice: Int,
    var quantity: Int = 1,
    var isSelected: Boolean = false
)