package com.example.ecommerce.repository

import com.example.ecommerce.room.dao.NotificationDAO
import com.example.ecommerce.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationDAO: NotificationDAO
) {

    fun getAll(): Flow<List<NotificationEntity>> {
        return notificationDAO.getAll()
    }

    suspend fun insertToNotification(notificationEntity: NotificationEntity) {
        notificationDAO.insertNotification(notificationEntity)
    }

    suspend fun updateRead(idNotification: Long, isRead: Boolean) {
        notificationDAO.updateRead(idNotification, isRead)
    }

    fun cekIsRead(isRead: Boolean): Flow<List<NotificationEntity>> {
        return notificationDAO.cekIsRead(isRead)
    }
}