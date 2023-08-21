package com.example.ecommerce.model

import android.content.Context

class PrefHelper (context: Context) {

    companion object{
        const val SP_NAME = "profile_pref"
        const val NAME = "nama"
        const val IMAGE = "image url"
        const val TOKEN = "token"
        const val REFRESH_TOKEN = "refresh_token"
        const val OB_CHECK = "onboarding_check"
    }

    val prefrence = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun setProfile(profilePref: ProfilePref){
        val prefEditor = prefrence.edit()

        prefEditor.putString(NAME, profilePref.name)
        prefEditor.putString(IMAGE, profilePref.image)
        prefEditor.putString(TOKEN, profilePref.token)
        prefEditor.putString(REFRESH_TOKEN, profilePref.refreshToken)
        prefEditor.putBoolean(OB_CHECK, profilePref.obCheck)
    }

    fun getProfile(): ProfilePref{
        val profile = ProfilePref()
        profile.name = prefrence.getString(NAME, "").toString()
        profile.image = prefrence.getString(IMAGE, "").toString()
        profile.token = prefrence.getString(TOKEN, "").toString()
        profile.refreshToken = prefrence.getString(REFRESH_TOKEN, "").toString()
        profile.obCheck = prefrence.getBoolean(OB_CHECK, false)

        return profile
    }
}