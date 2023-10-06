package com.example.ecommerce

import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.auth.CekAuthorization
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cekAuthorization: CekAuthorization
) : ViewModel() {

    val test = cekAuthorization.isDataLoaded

    fun resetAuthorization() {
        cekAuthorization.resetAuthorization()
    }

}