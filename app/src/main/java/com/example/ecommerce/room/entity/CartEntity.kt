package com.example.ecommerce.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.ui.main.checkout.CheckoutItem
import kotlinx.parcelize.Parcelize

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
    val variantName: String,
    val variantPrice: Int,
    var quantity: Int = 1,
    var isSelected: Boolean = false
)


fun CartEntity.cartEntityToChekoutItem(): CheckoutItem {
    val checkoutItem = CheckoutItem(
        productId,
        productName,
        productPrice,
        image,
        brand,
        description,
        store,
        sale,
        stock,
        totalRating,
        totalReview,
        totalSatisfaction,
        productRating,
        variantName,
        variantPrice
    )
    return checkoutItem
}