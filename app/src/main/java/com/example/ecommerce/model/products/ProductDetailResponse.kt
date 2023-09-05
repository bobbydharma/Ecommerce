package com.example.ecommerce.model.products

import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.room.entity.WishlistEntity

data class ProductDetailResponse (
    val code: Int,
    val message: String,
    val data: DataProductDetail
)

data class DataProductDetail (
    val productId: String,
    val productName: String,
    var productPrice: Int,
    val image: List<String>,
    val brand: String,
    val description: String,
    val store: String,
    val sale: Int,
    val stock: Int,
    val totalRating: Int,
    val totalReview: Int,
    val totalSatisfaction: Int,
    val productRating: Float,
    val productVariant: List<ProductVariant>
)

data class ProductVariant (
    val variantName: String,
    val variantPrice: Int
)

fun DataProductDetail.mappingCart(index:Int): CartEntity {
    val cartEntity = CartEntity(
        productId,
        productName,
        productPrice,
        image[0],
        brand,
        description,
        store,
        sale,
        stock,
        totalRating,
        totalReview,
        totalSatisfaction,
        productRating,
        productVariant[index].variantName,
        productVariant[index].variantPrice
    )
    return cartEntity
}

fun DataProductDetail.mappingWishlist(): WishlistEntity {
    val wishlistEntity = WishlistEntity(
        productId,
        productName,
        productPrice,
        image[0],
        brand,
        description,
        store,
        sale,
        stock,
        totalRating,
        totalReview,
        totalSatisfaction,
        productRating,
        productVariant[0].variantName,
        productVariant[0].variantPrice
    )
    return wishlistEntity
}