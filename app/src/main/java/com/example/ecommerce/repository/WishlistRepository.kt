package com.example.ecommerce.repository

import com.example.ecommerce.core.room.dao.WishlistDAO
import com.example.ecommerce.core.room.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistRepository @Inject constructor(
    private val wishlistDAO: WishlistDAO
) {
    fun getAll(): Flow<List<WishlistEntity>> {
        return wishlistDAO.getAll()
    }

    suspend fun insertToWishlist(wishlistEntity: WishlistEntity) {
        wishlistDAO.insertWishlist(wishlistEntity)
    }

    suspend fun deleteWishlist(wishlistEntity: WishlistEntity) {
        wishlistDAO.deleteWishlist(wishlistEntity)
    }

    fun cekItemWishlist(productId: String): Flow<WishlistEntity?> {
        return wishlistDAO.cekItemWishlist(productId)
    }
}