package com.example.ecommerce.ui.main.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.core.app.ActivityCompat.recreate
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.room.AppDatabase
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.resources.MaterialAttributes
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper
    @Inject
    lateinit var database: AppDatabase
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedPreferencesManager.dark_theme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogoutHome.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                database.clearAllTables()
                firebaseAnalytics.logEvent("BUTTON_CLICK"){
                    param("BUTTON_NAME", "Home_Logout" )
                }
            }

            sharedPreferencesManager.logout()
            (requireActivity() as MainActivity).logout()
        }

        binding.switchLanguage.apply {
            setOnCheckedChangeListener {_, isChecked ->
                val check = if (isChecked) "in" else "en"
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(check)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }

        }

        binding.switchTheme.isChecked = sharedPreferencesManager.dark_theme

        binding.switchTheme.apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferencesManager.dark_theme = true
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferencesManager.dark_theme = false
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (AppCompatDelegate.getApplicationLocales().get(0)?.language != null){
            binding.switchLanguage.isChecked = AppCompatDelegate.getApplicationLocales().get(0)!!.language == "in"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}