package com.example.ecommerce.ui.main.checkout

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.ecommerce.room.entity.CartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) :ViewModel() {

    private var _itemCheckoutList = MutableStateFlow(savedStateHandle.get<CheckoutList>("CheckoutList"))
    val itemCheckoutList: StateFlow<CheckoutList?> = _itemCheckoutList

//    fun updateCheckoutVariable(newVariableValue: Int, productId : Int) {
//        for
//    }
//
//    fun updateItemCheckoutList(newCheckoutList: CheckoutItem) {
//        _itemCheckoutList.value?.item = listOf(newCheckoutList)
//    }

}