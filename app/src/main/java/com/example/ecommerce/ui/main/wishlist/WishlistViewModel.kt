package com.example.ecommerce.ui.main.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.products.mappingCart
import com.example.ecommerce.core.room.entity.CartEntity
import com.example.ecommerce.core.room.entity.WishlistEntity
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val wishlistItem = wishlistRepository.getAll()

    suspend fun deleteWishlist(wishlistEntity: WishlistEntity) {
        viewModelScope.launch {
            wishlistRepository.deleteWishlist(wishlistEntity)
        }
    }

    suspend fun insertOrUpdateItem(
        product: com.example.ecommerce.core.model.products.DataProductDetail,
        index: Int
    ) {
        viewModelScope.launch {
            val cartItem = product.mappingCart(index)
            cartRepository.insertOrUpdateItem(cartItem)
        }
    }

    suspend fun cekItem(productId: String): CartEntity? {
        return cartRepository.cekItem(productId)
    }
}