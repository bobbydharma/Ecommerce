package com.example.ecommerce.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.room.dao.CartDAO
import com.example.ecommerce.room.dao.WishlistDAO
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.room.entity.WishlistEntity

@Database(entities = [CartEntity::class, WishlistEntity::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDAO() : CartDAO
    abstract fun wishlistDAO() : WishlistDAO

}