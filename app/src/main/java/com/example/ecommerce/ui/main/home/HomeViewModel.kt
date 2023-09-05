package com.example.ecommerce.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository
): ViewModel() {

    suspend fun deleteAllData(){
        viewModelScope.launch {
            wishlistRepository.deleteAllDataWishlist()
            cartRepository.deleteAllDataCart()
        }
    }

}