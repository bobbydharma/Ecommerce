package com.example.ecommerce.di

import com.example.ecommerce.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val instance:APIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(APIService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(APIService::class.java)
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): APIService =
        retrofit.create(APIService::class.java)

}