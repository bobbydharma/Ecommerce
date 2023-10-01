package com.example.ecommerce.ui.main.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.ui.main.payment.Item
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MainRepository
) : ViewModel() {

    private var _itemCheckoutList =
        MutableStateFlow(savedStateHandle.get<CheckoutList>("CheckoutList"))
    val itemCheckoutList: StateFlow<CheckoutList?> = _itemCheckoutList

    private val _fulfillment = MutableLiveData<Result<FulfillmentResponse>>()
    val fulfillment: LiveData<Result<FulfillmentResponse>> = _fulfillment

    lateinit var dataItemPayment: Item

    suspend fun postFulfillment(fulfillmentRequest: FulfillmentRequest) {
        _fulfillment.value = Result.Loading
        viewModelScope.launch {
            val result = repository.postFulfillment(fulfillmentRequest)
            _fulfillment.value = result
        }

    }

}