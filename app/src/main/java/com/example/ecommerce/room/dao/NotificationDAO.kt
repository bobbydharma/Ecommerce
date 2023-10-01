package com.example.ecommerce.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.room.entity.NotificationEntity
import com.example.ecommerce.room.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDAO {

    @Query("SELECT * FROM notification ORDER BY idNotification DESC")
    fun getAll(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notificationEntity: NotificationEntity)

    @Query("UPDATE notification SET isRead = :isRead WHERE idNotification =  :idNotification")
    suspend fun updateRead(idNotification: Long, isRead: Boolean)

    @Query("SELECT * FROM notification WHERE isRead = :isRead ")
    fun cekIsRead(isRead: Boolean): Flow<List<NotificationEntity>>

}