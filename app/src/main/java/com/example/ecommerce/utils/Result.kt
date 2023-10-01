package com.example.ecommerce.utils

import java.text.NumberFormat

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

fun Int.formatToIDR(): String {
    val localeID = java.util.Locale("in", "ID")
    val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
    return currencyFormatter.format(this).replace(",00", "")
}