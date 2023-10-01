package com.example.ecommerce.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ecommerce.R
import com.example.ecommerce.auth.AuthAuthenticator
import com.example.ecommerce.auth.AuthInterceptor
import com.example.ecommerce.auth.CekAuthorization
import com.example.ecommerce.network.APIService
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.repository.NotificationRepository
import com.example.ecommerce.room.AppDatabase
import com.example.ecommerce.room.dao.CartDAO
import com.example.ecommerce.room.dao.NotificationDAO
import com.example.ecommerce.room.dao.WishlistDAO
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "App_Database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDAO {
        return database.cartDAO()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: AppDatabase): NotificationDAO {
        return database.notificationDAO()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(database: AppDatabase): WishlistDAO {
        return database.wishlistDAO()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(prefHelper: PrefHelper): AuthInterceptor =
        AuthInterceptor(prefHelper)

    @Singleton
    @Provides
    fun provideAuthenticator(
        @ApplicationContext context: Context,
        prefHelper: PrefHelper,
        cekAuthorization: CekAuthorization
    ): Authenticator {
        return AuthAuthenticator(context, prefHelper, cekAuthorization)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun provideRetrofit(
        @ApplicationContext context: Context,
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(interceptor)
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

    @Singleton
    @Provides
    fun remoteConfig(): FirebaseRemoteConfig {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        return remoteConfig
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }


}