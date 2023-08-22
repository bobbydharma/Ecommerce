package com.example.ecommerce.network

import com.example.ecommerce.model.ProfileRequest
import com.example.ecommerce.model.ProfileResponse
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIService {

    companion object{
        const val BASE_URL = "http://172.17.20.166:5000/"
        const val API_KEY = "6f8856ed-9189-488f-9011-0ff4b6c08edc"
    }

    @POST("register")
    suspend fun postRegister(
        @Header("API_KEY") API_KEY :String,
        @Body userRequest: UserRequest
    ): Response<UserResponse>

    @Multipart
    @POST("profile")
    fun postProfile(
        @Header("Authorization") authorization: String,
        @Part userName: MultipartBody.Part,
        @Part userImage: MultipartBody.Part
    ): Response<ProfileResponse>

    @GET("login")
    suspend fun getLogin(
        @Header("API_KEY") API_KEY :String,
        @Body userRequest: UserRequest
    ): Response<UserResponse>

}