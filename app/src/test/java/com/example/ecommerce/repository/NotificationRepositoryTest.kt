package com.example.ecommerce.repository

import com.example.ecommerce.core.room.dao.CartDAO
import com.example.ecommerce.core.room.dao.NotificationDAO
import com.example.ecommerce.core.room.entity.NotificationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class NotificationRepositoryTest {

    private lateinit var notificationDAO: com.example.ecommerce.core.room.dao.NotificationDAO
    private lateinit var notificationRepository: NotificationRepository

    @Before
    fun setup() {
        notificationDAO = Mockito.mock()
        notificationRepository = NotificationRepository(notificationDAO)
    }

    private val notification = com.example.ecommerce.core.room.entity.NotificationEntity(
        idNotification = 1,
        title = "title",
        body = "body",
        image = "image",
        type = "Type",
        date = "date",
        time = "Time",
        isRead = false
    )

    @Test
    fun getAllNotificationRepositoryTest() = runTest {
        whenever(notificationDAO.getAll()).thenReturn(flowOf(listOf(notification)))
        val result = notificationRepository.getAll()
        Assert.assertEquals(listOf(notification), result.first())
    }

    @Test
    fun insertToNotificationNotificationRepositoryTest() = runTest {
        whenever(notificationDAO.insertNotification(notification)).thenReturn(Unit)

        val result = notificationRepository.insertToNotification(notification)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun updateReadNotificationRepositoryTest() = runTest {
        val idNotification: Long = 1
        val isRead = true
        whenever(notificationDAO.updateRead(idNotification, isRead)).thenReturn(Unit)

        val result = notificationRepository.updateRead(idNotification, isRead)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun cekIsReadNotificationRepositoryTest() = runTest {
        val isRead = true
        whenever(notificationDAO.cekIsRead(isRead)).thenReturn(flowOf(listOf(notification)))

        val result = notificationRepository.cekIsRead(isRead)
        Assert.assertEquals(listOf(notification), result.first())
    }
}