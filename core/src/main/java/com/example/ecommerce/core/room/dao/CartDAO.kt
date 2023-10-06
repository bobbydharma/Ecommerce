package com.example.ecommerce.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDAO {

    @Query("SELECT * FROM cart")
    fun getAll(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE productId = :productId ")
    suspend fun cekItem(productId: String): CartEntity?

    @Query("SELECT * FROM cart WHERE productId = :productId ")
    fun getItem(productId: String): Flow<CartEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartEntity: CartEntity)

    @Delete
    suspend fun deleteCart(vararg cartEntity: CartEntity)

    @Query("UPDATE cart SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantityCart(productId: String, quantity: Int)

    @Query("UPDATE cart SET isSelected = :isSelected WHERE productId =  :productId")
    suspend fun updateSelectedCart(productId: String, isSelected: Boolean)

    @Query("UPDATE cart SET isSelected = :isSelected ")
    suspend fun updateAllSelectedCart(isSelected: Boolean)

    @Query("DELETE FROM cart")
    suspend fun deleteAllDataCart()

}