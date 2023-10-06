package com.example.ecommerce.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.core.room.dao.CartDAO
import com.example.ecommerce.core.room.dao.NotificationDAO
import com.example.ecommerce.core.room.dao.WishlistDAO
import com.example.ecommerce.core.room.entity.CartEntity
import com.example.ecommerce.core.room.entity.NotificationEntity
import com.example.ecommerce.core.room.entity.WishlistEntity

@Database(
    entities = [CartEntity::class, WishlistEntity::class, NotificationEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDAO(): CartDAO
    abstract fun wishlistDAO(): WishlistDAO
    abstract fun notificationDAO(): NotificationDAO

}