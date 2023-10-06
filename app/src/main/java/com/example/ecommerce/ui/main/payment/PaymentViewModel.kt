package com.example.ecommerce.ui.main.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.model.checkout.PaymentResponse
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.utils.Result
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: MainRepository,
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _paymentItem = MutableLiveData<Result<PaymentResponse>>()
    val paymentItem: LiveData<Result<PaymentResponse>> = _paymentItem

    private val _stringPayment = MutableLiveData<Result<String>>()
    val stringPayment: LiveData<Result<String>> = _stringPayment

    init {
        fetchAndActivateRemoteConfig()
        updateListener()
    }

//    fun postPayment() {
//        _paymentItem.value = Result.Loading
//        viewModelScope.launch {
//            val result = repository.postPayment()
//            _paymentItem.value = result
//        }
//    }

    fun fetchAndActivateRemoteConfig() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data = remoteConfig.getString("Payment")
                    _stringPayment.value = Result.Success(data)
                    Log.d("fetchAndActivateRemoteConfig", "if")
                } else {
                    val data = remoteConfig.getString("Payment")
                    _stringPayment.value = Result.Success(data)
                    Log.d("fetchAndActivateRemoteConfig", "else")
                }
            }
    }

    fun updateListener() {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {

                if (configUpdate.updatedKeys.contains("Payment")) {
                    remoteConfig.activate().addOnCompleteListener {
                        val data = remoteConfig.getString("Payment")
                        _stringPayment.value = Result.Success(data)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                val data = remoteConfig.getString("Payment")
                _stringPayment.value = Result.Success(data)
                Log.d("onError", "onError")
            }
        })
    }

}