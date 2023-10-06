package com.example.ecommerce.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.products.DataProductDetail
import com.example.ecommerce.core.model.products.ProductDetailResponse
import com.example.ecommerce.core.model.products.mappingCart
import com.example.ecommerce.core.model.products.mappingWishlist
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.repository.WishlistRepository
import com.example.ecommerce.core.room.entity.CartEntity
import com.example.ecommerce.core.room.entity.WishlistEntity
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val repository: MainRepository,
    private val cartRepository: CartRepository,
    private val savedStateHandle: SavedStateHandle,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private var _detailProduct =
        MutableLiveData<Result<com.example.ecommerce.core.model.products.ProductDetailResponse>>()
    val detailProduct: LiveData<Result<com.example.ecommerce.core.model.products.ProductDetailResponse>> =
        _detailProduct

    val id = savedStateHandle.get<String>("id_product") ?: ""

    private val _wishlistItem =
        MutableStateFlow<com.example.ecommerce.core.room.entity.WishlistEntity?>(null)
    val wishlistItem: StateFlow<com.example.ecommerce.core.room.entity.WishlistEntity?> =
        _wishlistItem

    private val _cartItem =
        MutableStateFlow<com.example.ecommerce.core.room.entity.CartEntity?>(null)
    val cartItem: StateFlow<com.example.ecommerce.core.room.entity.CartEntity?> = _cartItem

    private val _cartEntityFlow =
        MutableStateFlow<com.example.ecommerce.core.room.entity.CartEntity?>(null)
    val cartEntityFlow: StateFlow<com.example.ecommerce.core.room.entity.CartEntity?> =
        _cartEntityFlow

    init {
        getDetailProduct(id)
        fetchData(id)
        getCart(id)
    }

    fun getDetailProduct(id: String) {
        _detailProduct.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getProductDetail(id)
            _detailProduct.value = result
        }
    }

    suspend fun cekItem(productId: String): com.example.ecommerce.core.room.entity.CartEntity? {
        return cartRepository.cekItem(productId)
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

    fun insertToWishlist(
        product: com.example.ecommerce.core.model.products.DataProductDetail,
        index: Int
    ) {
        viewModelScope.launch {
            val wishlistItem = product.mappingWishlist(index)
            wishlistRepository.insertToWishlist(wishlistItem)
        }
    }

    fun deleteWishlist(product: com.example.ecommerce.core.model.products.DataProductDetail) {
        viewModelScope.launch {
            val wishlistItem = product.mappingWishlist(0)
            wishlistRepository.deleteWishlist(wishlistItem)
        }
    }

    fun fetchData(productId: String) {
        viewModelScope.launch {
            wishlistRepository.cekItemWishlist(productId).collect { wishlistEntity ->
                _wishlistItem.value = wishlistEntity
            }
        }
    }

    fun getCart(productId: String) {
        viewModelScope.launch {
            cartRepository.getItem(productId).collect { cartEntity ->
                _cartEntityFlow.value = cartEntity
            }
        }
    }
}