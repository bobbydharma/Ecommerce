package com.example.ecommerce.core.model.checkout

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.ecommerce.core.model.transaction.Datum
import com.example.ecommerce.core.model.transaction.Item
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class PaymentResponse(
    var code: Int,
    var message: String,
    var data: List<DataPayment>
) : Parcelable
@Keep
@Parcelize
data class DataPayment(
    var title: String,
    var item: List<ItemPayment>
) : Parcelable
@Keep
@Parcelize
data class ItemPayment(
    var label: String,
    var image: String,
    var status: Boolean
) : Parcelable