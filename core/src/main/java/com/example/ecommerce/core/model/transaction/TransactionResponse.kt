package com.example.ecommerce.core.model.transaction

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.ecommerce.core.model.checkout.Data
import kotlinx.parcelize.Parcelize

@Keep
data class TransactionResponse(
    val code: Int,
    val message: String,
    val data: List<Datum>
)
@Keep
@Parcelize
data class Datum(
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    val items: List<Item>,
    val rating: Int?,
    val review: String,
    val image: String,
    val name: String
) : Parcelable
@Keep
@Parcelize
data class Item(
    val productId: String,
    val variantName: String,
    val quantity: Long
) : Parcelable

fun Datum.convertToDataFulfillment(): Data {
    val data = Data(
        invoiceId,
        status,
        date,
        time,
        payment,
        total,
        rating,
        review
    )
    return data
}