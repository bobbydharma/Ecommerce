package com.example.ecommerce.core.model.products

import androidx.annotation.Keep
import com.example.ecommerce.core.model.checkout.CheckoutItem
import com.example.ecommerce.core.model.checkout.CheckoutList
import com.example.ecommerce.core.room.entity.CartEntity
import com.example.ecommerce.core.room.entity.WishlistEntity

@Keep
data class ProductDetailResponse(
    val code: Int,
    val message: String,
    val data: DataProductDetail
)

@Keep
data class DataProductDetail(
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

@Keep
data class ProductVariant(
    val variantName: String,
    val variantPrice: Int
)

fun DataProductDetail.mappingCart(index: Int): CartEntity {
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

fun DataProductDetail.mappingWishlist(index: Int): WishlistEntity {
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
        productVariant[index].variantName,
        productVariant[index].variantPrice
    )
    return wishlistEntity
}

fun DataProductDetail.convertToCheckoutList(index: Int): CheckoutList {
    val checkoutList = mutableListOf<CheckoutItem>()
    checkoutList.add(
        CheckoutItem(
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
    )
    return CheckoutList(checkoutList)
}