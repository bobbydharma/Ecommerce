package com.example.ecommerce.ui.main.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.core.room.entity.CartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    val cartItem = repository.getAll()

    suspend fun deleteCart(vararg cartEntity: CartEntity) {
        viewModelScope.launch {
            repository.deleteCart(*cartEntity)
        }
    }

    suspend fun updateQuantityCart(productId: String, quantity: Int) {
        viewModelScope.launch {
            repository.updateQuantityCart(productId, quantity)
        }
    }


    suspend fun updateSelectedCart(productId: String, isSelected: Boolean) {
        viewModelScope.launch {
            repository.updateSelectedCart(productId, isSelected)
        }
    }

    suspend fun updateAllSelectedCart(isSelected: Boolean) {
        viewModelScope.launch {
            repository.updateAllSelectedCart(isSelected)
        }
    }

}