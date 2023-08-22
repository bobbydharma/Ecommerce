package com.example.ecommerce.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ecommerce.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @ApplicationContext context: Context
    ): Retrofit {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(APIService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }
}