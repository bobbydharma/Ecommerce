package com.example.ecommerce.model

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefHelper @Inject constructor (private val sharedPreferences: SharedPreferences) {

    companion object{
        const val SP_NAME = "profile_pref"
        const val KEY_NAMA = "nama"
        const val KEY_IMAGE = "image_url"
        const val KEY_TOKEN = "token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_OB_CHECK = "ob_check"
    }

    var nama: String?
        get() = sharedPreferences.getString(KEY_NAMA, null)
        set(value) = sharedPreferences.edit().putString(KEY_NAMA, value).apply()

    var image_url: String?
        get() = sharedPreferences.getString(KEY_IMAGE, null)
        set(value) = sharedPreferences.edit().putString(KEY_IMAGE, value).apply()

    var token: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(KEY_TOKEN, value).apply()

    var refreshToken: String?
        get() = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        set(value) = sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, value).apply()

    var obCheck: Boolean
        get() = sharedPreferences.getBoolean(KEY_OB_CHECK, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_OB_CHECK, value).apply()

}