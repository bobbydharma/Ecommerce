package com.example.ecommerce.ui.main.notifcation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) :ViewModel() {

    val notificationItem = repository.getAll()

    fun updateRead(idNotification: Long, isRead: Boolean) {
        viewModelScope.launch {
            repository.updateRead(idNotification, isRead)
        }
    }
}