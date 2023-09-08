package com.example.ecommerce.ui.main.transaction

import android.os.Parcelable
import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.ui.main.checkout.Data
import kotlinx.parcelize.Parcelize

data class TransactionResponse (
    val code: Int,
    val message: String,
    val data: List<Datum>
)

@Parcelize
data class Datum (
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    val items: List<Item>,
    val rating: Int,
    val review: String,
    val image: String,
    val name: String
):Parcelable

@Parcelize
data class Item (
    val productId: String,
    val variantName: String,
    val quantity: Long
): Parcelable

fun Datum.convertToDataFulfillment(): Data {
    val data = Data(
        invoiceId,
        status,
        date,
        time,
        payment,
        total
    )
    return data
}