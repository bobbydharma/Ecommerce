package com.example.ecommerce.ui.main.payment

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class PaymentResponse(
    var code: Int,
    var message: String,
    var data: List<Datum>
) : Parcelable
@Keep
@Parcelize
data class Datum(
    var title: String,
    var item: List<Item>
) : Parcelable
@Keep
@Parcelize
data class Item(
    var label: String,
    var image: String,
    var status: Boolean
) : Parcelable