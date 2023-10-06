package com.example.ecommerce.core.model.checkout

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CheckoutItem(
    var productId: String,
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
) : Parcelable
@Keep
@Parcelize
data class CheckoutList(
    var itemCheckout: List<CheckoutItem>
) : Parcelable

fun List<com.example.ecommerce.core.room.entity.CartEntity>.toChekoutList(): CheckoutList {
    val checkoutList = mutableListOf<CheckoutItem>()
    this.forEach {
        checkoutList.add(
            CheckoutItem(
                it.productId,
                it.productName,
                it.productPrice,
                it.image,
                it.brand,
                it.description,
                it.store,
                it.sale,
                it.stock,
                it.totalRating,
                it.totalReview,
                it.totalSatisfaction,
                it.productRating,
                it.variantName,
                it.variantPrice,
                it.quantity,
                it.isSelected
            )
        )
    }
    return CheckoutList(checkoutList)
}

fun List<CheckoutItem>.toFulfillmentItem(): List<ItemFullfillment> {
    val fulfilmentItems = mutableListOf<ItemFullfillment>()
    this.forEach {
        fulfilmentItems.add(
            ItemFullfillment(
                it.productId,
                it.varianName,
                it.quantity
            )
        )
    }
    return fulfilmentItems
}
