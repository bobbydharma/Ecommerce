package com.example.ecommerce.auth

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ecommerce.model.user.UserResponse
import com.example.ecommerce.network.APIService
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefHelper: PrefHelper,
    private val cekAuthorization: CekAuthorization
): Authenticator {

//    192.168.153.125
    companion object {
        const val BASE_URL = "http://192.168.153.125:5000/"
        const val API_KEY = "6f8856ed-9189-488f-9011-0ff4b6c08edc"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this){
            return  runBlocking {
                try {
                    val token = prefHelper.refreshToken
                    val newToken =  getNewToken(token ?: "")
                    if (newToken.isSuccessful && newToken.body() != null) {
                        newToken?.body()?.let {
                            prefHelper.token = it.data.accessToken
                            response.request.newBuilder()
                                .header("Authorization", "Bearer ${token}")
                                .build()
                        }
                    }else{
                        if (newToken.code() == 401){
                            Log.d("error authenticator", "401")
                            cekAuthorization.setAutorization()
                            null
                        }else{
                            null
                        }
                    }
                }catch (e: HttpException){
                    null
                }
            }
        }

    }


    suspend fun getNewToken(token: String): retrofit2.Response<UserResponse>{
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(APIService::class.java)
        return service.postRefresh(API_KEY, hashMapOf("token" to token))
    }
}