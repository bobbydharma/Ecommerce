package com.example.ecommerce.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.NumberFormat
import kotlin.coroutines.CoroutineContext

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object Empty : Result<Nothing>()
}

suspend fun <T> MutableStateFlow<Result<T>>.asMutableStateFlow(
    action: suspend () -> T
) {
    this.update { Result.Loading }
    try {
        val data = action()
        if (data != null) {
            this.update { Result.Success(data) }
        } else {
            this.update { Result.Empty }
        }

    } catch (error: Throwable) {
        this.update { Result.Error(error) }
    }
}

suspend fun <T> safeApiCall(
    dispatcher: CoroutineContext = Dispatchers.IO,
    apiCall: suspend () -> T?
): T {
    return withContext(dispatcher) {
        try {
            apiCall() ?: throw EmptyException("Network null")
        } catch (error: Throwable) {
            throw error
        }
    }
}

data class EmptyException(override val message: String) : Exception(message)

fun Int.formatToIDR(): String {
    val localeID = java.util.Locale("in", "ID")
    val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
    return currencyFormatter.format(this).replace(",00", "")
}