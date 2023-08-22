package com.example.ecommerce.utils

import java.lang.Exception
import kotlin.Result

sealed class Result <out T> {
    data class Success <out T>(val data: T) : com.example.ecommerce.utils.Result<T>()
    data class Error(val exception: Throwable) : com.example.ecommerce.utils.Result<Nothing>()
    object Loading : com.example.ecommerce.utils.Result<Nothing>()
}