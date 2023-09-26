package com.example.ecommerce.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ecommerce.room.dao.CartDAO
import com.example.ecommerce.room.dao.NotificationDAO
import com.example.ecommerce.room.entity.NotificationEntity
import com.example.ecommerce.room.entity.WishlistEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class NotificationDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var notificationDAO: NotificationDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        notificationDAO = database.notificationDAO()
    }

    @After
    fun teardown() {
        database.close()
    }

    private val notification = NotificationEntity(
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
    fun insertAndGetAllNotificationItems() = runTest {
        notificationDAO.insertNotification(notification)
        val allWishlistItems = notificationDAO.getAll().first()
        Assert.assertEquals(listOf(notification), allWishlistItems)
    }

    @Test
    fun updateRead() = runTest {
        val idNotification : Long = 1
        val isRead = true
        notificationDAO.insertNotification(notification)
        notificationDAO.updateRead(idNotification, isRead)
        val allWishlistItems = notificationDAO.getAll().first()
        Assert.assertEquals(isRead, allWishlistItems[0].isRead)
    }

    @Test
    fun cekIsRead() = runTest {
        val idNotification : Long = 1
        val isRead = true
        notificationDAO.insertNotification(notification)
        notificationDAO.updateRead(idNotification, isRead)
        val allWishlistItems = notificationDAO.cekIsRead(true).first()
        Assert.assertEquals(isRead, allWishlistItems[0].isRead)
    }

}