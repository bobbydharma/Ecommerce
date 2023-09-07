package com.example.ecommerce.ui.main.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.user.LoginResponse
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _paymentItem = MutableLiveData<Result<PaymentResponse>>()
    val paymentItem: LiveData<Result<PaymentResponse>> = _paymentItem

    fun postPayment() {
        _paymentItem.value = Result.Loading
        viewModelScope.launch {
            val result = repository.postPayment()
            _paymentItem.value = result
        }
    }

}