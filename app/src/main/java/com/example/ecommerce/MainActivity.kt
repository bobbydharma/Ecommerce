package com.example.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.room.AppDatabase
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

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.test.observe(this){
            if (it == true){
                GlobalScope.launch(Dispatchers.IO){
                    database.clearAllTables()
                }
                sharedPreferencesManager.logout()
                viewModel.resetAuthorization()
                logout()
            }
        }

    }

    fun logout(){
        navController.navigate(R.id.main_to_prelogin)
    }
}