package com.example.ecommerce.ui.prelogin.login

import androidx.lifecycle.ViewModel
import com.example.ecommerce.network.PreloginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor (private val preloginRepository: PreloginRepository) : ViewModel() {
}