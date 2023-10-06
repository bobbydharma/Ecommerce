package com.example.ecommerce.utils

import com.example.ecommerce.core.model.user.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException

object ParsingError {
    val gson = Gson()
    fun ResponseBody.getThrowable() : String{
            val errorResponse = this.string()
            val data = gson.fromJson(errorResponse, ErrorResponse::class.java)
            return data.message
    }

}