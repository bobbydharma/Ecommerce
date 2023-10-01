package com.example.ecommerce.repository

import com.example.ecommerce.room.dao.CartDAO
import com.example.ecommerce.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDAO: CartDAO
) {

    fun getAll(): Flow<List<CartEntity>> {
        return cartDAO.getAll()
    }

    suspend fun insertOrUpdateItem(cart: CartEntity) {
        val existingCartItem = cartDAO.cekItem(cart.productId)
        if (existingCartItem != null) {
            if (existingCartItem.stock > existingCartItem.quantity) {
                existingCartItem.quantity += cart.quantity
                cartDAO.updateQuantityCart(existingCartItem.productId, existingCartItem.quantity)
            } else {
            }
        } else {
            cartDAO.insertCart(cart)
        }
    }

    suspend fun cekItem(productId: String): CartEntity? {
        return cartDAO.cekItem(productId)
    }

    fun getItem(productId: String): Flow<CartEntity?> {
        return cartDAO.getItem(productId)
    }

    suspend fun deleteCart(vararg cartEntity: CartEntity) {
        cartDAO.deleteCart(*cartEntity)
    }

    suspend fun updateQuantityCart(productId: String, quantity: Int) {
        cartDAO.updateQuantityCart(productId, quantity)
    }

    suspend fun updateSelectedCart(productId: String, isSelected: Boolean) {
        cartDAO.updateSelectedCart(productId, isSelected)
    }

    suspend fun updateAllSelectedCart(isSelected: Boolean) {
        cartDAO.updateAllSelectedCart(isSelected)
    }

    suspend fun deleteAllDataCart() {
        cartDAO.deleteAllDataCart()
    }
}