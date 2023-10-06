package com.example.ecommerce.core.preference

import android.content.SharedPreferences
import javax.inject.Inject

class PrefHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val KEY_NAMA = "nama"
        const val KEY_IMAGE = "image_url"
        const val KEY_TOKEN = "token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_OB_CHECK = "ob_check"
        const val DARK_THEME = "dark_theme"
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

    var dark_theme: Boolean
        get() = sharedPreferences.getBoolean(DARK_THEME, false)
        set(value) = sharedPreferences.edit().putBoolean(DARK_THEME, value).apply()

    fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("nama")
        editor.remove("image_url")
        editor.remove("token")
        editor.remove("refresh_token")
        editor.apply()
        editor.commit()
    }

}