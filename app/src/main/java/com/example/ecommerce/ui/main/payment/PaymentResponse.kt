package com.example.ecommerce.ui.main.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentResponse (
    var code: Int,
    var message: String,
    var data: List<Datum>
):Parcelable

@Parcelize
data class Datum (
    var title: String,
    var item: List<Item>
):Parcelable

@Parcelize
data class Item (
    var label: String,
    var image: String,
    var status: Boolean
):Parcelable