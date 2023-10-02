package com.example.ecommerce.room.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.model.products.ProductVariant

@Keep
@Entity(tableName = "Wishlist")
data class WishlistEntity(
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
)

fun WishlistEntity.convertToDetail(): DataProductDetail {
    val variant = ProductVariant(
        varianName,
        varianPrice
    )
    val data = DataProductDetail(
        productId,
        productName,
        productPrice,
        listOf(image),
        brand,
        description,
        store,
        sale,
        stock,
        totalRating,
        totalReview,
        totalSatisfaction,
        productRating,
        listOf(variant)
    )
    return data
}