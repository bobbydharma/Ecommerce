package com.example.ecommerce.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.room.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDAO {

    @Query("SELECT * FROM wishlist")
    fun getAll(): Flow<List<WishlistEntity>>

    @Query("SELECT * FROM wishlist WHERE productId = :productId ")
    fun cekItemWishlist(productId: String): Flow<WishlistEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(wishlistEntity: WishlistEntity)

    @Delete
    suspend fun deleteWishlist(wishlistEntity: WishlistEntity)

    @Query("DELETE FROM wishlist")
    suspend fun deleteAllDataWishlist()

}