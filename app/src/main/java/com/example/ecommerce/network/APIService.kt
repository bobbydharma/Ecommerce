package com.example.ecommerce.network

import com.example.ecommerce.model.products.ProductDetailResponse
import com.example.ecommerce.model.products.ProductsResponse
import com.example.ecommerce.model.products.ReviewProduct
import com.example.ecommerce.model.products.SearchResponse
import com.example.ecommerce.model.user.LoginResponse
import com.example.ecommerce.model.user.ProfileResponse
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.model.user.UserResponse
import com.example.ecommerce.ui.main.checkout.FulfillmentRequest
import com.example.ecommerce.ui.main.checkout.FulfillmentResponse
import com.example.ecommerce.ui.main.payment.PaymentResponse
import com.example.ecommerce.ui.main.sendreview.RatingRequest
import com.example.ecommerce.ui.main.sendreview.RatingResponse
import com.example.ecommerce.ui.main.transaction.TransactionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    companion object {
        const val BASE_URL = "http://172.17.20.121:5000/"
    }

    @POST("register")
    suspend fun postRegister(
        @Header("API_KEY") API_KEY: String,
        @Body userRequest: UserRequest
    ): Response<UserResponse>

    @Multipart
    @POST("profile")
    suspend fun postProfile(
        @Part userImage: MultipartBody.Part,
        @Part userName: MultipartBody.Part
    ): Response<ProfileResponse>

    @POST("login")
    suspend fun postLogin(
        @Header("API_KEY") API_KEY: String,
        @Body userRequest: UserRequest
    ): Response<LoginResponse>

    @POST("products")
    suspend fun postProducts(
        @Query("search") search: String?,
        @Query("brand") brand: String?,
        @Query ("lowest") lowest: Int?,
        @Query ("highest") highest: Int?,
        @Query ("sort") sort: String?,
        @Query ("limit") limit: Int?,
        @Query ("page") page: Int?,
    ): Response<ProductsResponse>

    @POST("search")
    suspend fun postSearch(
        @Query("query") search: String?,
    ): Response<SearchResponse>

    @POST("refresh")
    suspend fun postRefresh(
        @Header("API_KEY") API_KEY: String,
        @Body token: HashMap<String,String>
    ): Response<UserResponse>

    @GET("products/{id}")
    suspend fun getDetailProduct(
        @Path ("id") id : String
    ): Response<ProductDetailResponse>

    @GET("review/{id}")
    suspend fun getReviewProduct(
        @Path ("id") id : String
    ): Response<ReviewProduct>

    @GET("payment")
    suspend fun postPayment(): Response<PaymentResponse>

    @POST("fulfillment")
    suspend fun postFulfillment(
        @Body items: FulfillmentRequest
    ) : Response<FulfillmentResponse>

    @POST("rating")
    suspend fun postRating(
        @Body ratingRequest : RatingRequest
    ) : Response<RatingResponse>

    @GET("transaction")
    suspend fun getTransaction() : TransactionResponse
}