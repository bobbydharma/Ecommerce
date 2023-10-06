package com.example.ecommerce.core.model.checkout

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
data class FulfillmentRequest(
    var payment: String,
    val items: List<ItemFullfillment>
)

@Keep
data class ItemFullfillment(
    val productId: String,
    val variantName: String,
    val quantity: Int
)
@Keep
@Parcelize
data class FulfillmentResponse(
    val code: Int,
    val message: String,
    val data: Data
) : Parcelable

@Keep
@Parcelize
data class Data(
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    var rating: Int?,
    var review: String?
    ) : Parcelable