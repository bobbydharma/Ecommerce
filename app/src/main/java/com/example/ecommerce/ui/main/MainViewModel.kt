package com.example.ecommerce.ui.main

import androidx.lifecycle.ViewModel
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.NotificationRepository
import com.example.ecommerce.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val cartItem = cartRepository.getAll()

    val itemWishlist = wishlistRepository.getAll()

    val itemNotification = notificationRepository.cekIsRead(false)

}