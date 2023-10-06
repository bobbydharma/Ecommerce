package com.example.ecommerce.sharedpreferences

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ecommerce.core.preference.PrefHelper
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SharedPreferencesTest {

    private lateinit var prefHelper: com.example.ecommerce.core.preference.PrefHelper

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        prefHelper = com.example.ecommerce.core.preference.PrefHelper(sharedPreferences)
    }

    @Test
    fun namePrefTest() {
        val expectedNama = "nama"
        prefHelper.nama = expectedNama
        assertEquals(prefHelper.nama, expectedNama)
    }

    @Test
    fun imagePrefTest() {
        val expectedImage = "image_url"
        prefHelper.image_url = expectedImage
        assertEquals(prefHelper.image_url, expectedImage)
    }

    @Test
    fun tokenPrefTest() {
        val expectedToken = "token"
        prefHelper.token = expectedToken
        assertEquals(prefHelper.token, expectedToken)
    }

    @Test
    fun refreshTokenPrefTest() {
        val expectedRefreshToken = "refresh_token"
        prefHelper.refreshToken = expectedRefreshToken
        assertEquals(prefHelper.refreshToken, expectedRefreshToken)
    }

    @Test
    fun obCekPrefTest() {
        val expectedOB = true
        prefHelper.obCheck = expectedOB
        assertEquals(prefHelper.obCheck, expectedOB)
    }

    @Test
    fun darkThemePrefTest() {
        val expectedDarkTheme = true
        prefHelper.dark_theme = expectedDarkTheme
        assertEquals(prefHelper.dark_theme, expectedDarkTheme)
    }

    @Test
    fun logoutPrefTest() {
        prefHelper.nama = "John Doe"
        prefHelper.image_url = "https://example.com/image.jpg"
        prefHelper.token = "12345"
        prefHelper.refreshToken = "67890"

        prefHelper.logout()

        assertNull(prefHelper.nama)
        assertNull(prefHelper.image_url)
        assertNull(prefHelper.token)
        assertNull(prefHelper.refreshToken)
    }

}