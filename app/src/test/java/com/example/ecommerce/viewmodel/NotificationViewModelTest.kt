package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.repository.NotificationRepository
import com.example.ecommerce.room.entity.NotificationEntity
import com.example.ecommerce.ui.main.notifcation.NotificationViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class NotificationViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: NotificationRepository
    private lateinit var notificationViewModel: NotificationViewModel

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

    val expectedNotificationFlow = flowOf(listOf(notification))

    @Before
    fun setup() {
        repository = Mockito.mock()

        whenever(repository.getAll()).thenReturn(expectedNotificationFlow)
        notificationViewModel = NotificationViewModel(repository)
    }

    @Test
    fun `get all notification notification viewmodel test`() = runTest {
        Assert.assertEquals(expectedNotificationFlow, notificationViewModel.notificationItem)
    }

    @Test
    fun `update read notification notification viewmodel test`() = runTest {
        val idNotification: Long = 1
        val isRead = true
        whenever(repository.updateRead(idNotification, isRead)).thenReturn(Unit)
        val resul = notificationViewModel.updateRead(idNotification, isRead)
        Assert.assertEquals(Unit, resul)
    }

}