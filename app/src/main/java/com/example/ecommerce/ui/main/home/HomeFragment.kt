package com.example.ecommerce.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ecommerce.MainActivity
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.core.room.AppDatabase
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.ui.main.store.StoreViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper

    private val viewModel by activityViewModels<StoreViewModel>()

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogoutHome.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                database.clearAllTables()
                firebaseAnalytics.logEvent("BUTTON_CLICK") {
                    param("BUTTON_NAME", "Home_Logout")
                }
            }
            clearViewModel()
            sharedPreferencesManager.logout()
            (requireActivity() as MainActivity).logout()
        }

        binding.switchLanguage.apply {
            setOnCheckedChangeListener { _, isChecked ->
                val check = if (isChecked) "in" else "en"
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(check)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }

        }

        binding.switchTheme.isChecked = sharedPreferencesManager.dark_theme

        binding.switchTheme.apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferencesManager.dark_theme = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferencesManager.dark_theme = false
                }
            }
        }

    }

    private fun clearViewModel() {
        viewModel.updateQuery(
            search = null,
            sort = null,
            brand = null,
            lowest = null,
            highest = null,
            sortId = null
        )
    }

    override fun onResume() {
        super.onResume()
        if (AppCompatDelegate.getApplicationLocales().get(0)?.language != null) {
            binding.switchLanguage.isChecked =
                AppCompatDelegate.getApplicationLocales().get(0)!!.language == "in"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}