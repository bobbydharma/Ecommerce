package com.example.ecommerce.core.base

import retrofit2.HttpException
import java.io.IOException

sealed interface DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>
    data class Error(val exception: Throwable? = null) : DataState<Nothing>
    object Loading : DataState<Nothing>
    object Empty : DataState<Nothing>
}