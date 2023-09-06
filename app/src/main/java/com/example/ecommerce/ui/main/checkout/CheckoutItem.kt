package com.example.ecommerce.ui.main.checkout

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.room.entity.CartEntity
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class CheckoutList(
    val item: List<CheckoutItem>
) : Parcelable

fun List<CartEntity>.toChekoutList() : CheckoutList {
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
