package com.example.ecommerce

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.room.AppDatabase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cekTheme()

        viewModel.test.observe(this) {
            if (it == true) {
                GlobalScope.launch(Dispatchers.IO) {
                    database.clearAllTables()
                }
                sharedPreferencesManager.logout()
                viewModel.resetAuthorization()
                logout()
            }
        }

        Firebase.messaging.subscribeToTopic("promo")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("TAG", "Gagal Berlangganan")
                } else {
                    Log.d("TAG", "berhasil Berlangganan")
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channelId"
            val channelName = "Channel human readable title"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW,
                ),
            )
        }

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.getString(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                // Tampilkan penjelasan kepada pengguna mengapa izin diperlukan
                // Misalnya, dengan menggunakan dialog atau pesan lainnya
            } else {
                // Minta izin secara langsung
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun cekTheme() {
        if (sharedPreferencesManager.dark_theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    fun logout() {
        navController.navigate(R.id.main_to_prelogin)
    }

    companion object {

        private const val TAG = "MainActivity"
    }
}