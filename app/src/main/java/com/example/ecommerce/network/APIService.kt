package com.example.ecommerce.network


import com.example.ecommerce.model.RegisterResponse
import com.example.ecommerce.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {

    companion object{
        const val BASE_URL = "http://172.20.10.3:5000/"
    }

    @POST("register")
    fun postRegister(
        @Header("API_KEY") API_KEY :String,
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @POST("profile")
    fun postProfile()

    @GET("login")
    suspend fun getLogin(
        @Header("API_KEY") API_KEY :String
    ): Call<RegisterResponse>

}