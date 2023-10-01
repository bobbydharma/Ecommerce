package com.example.ecommerce.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notification")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val idNotification: Long = 0,
    var title: String,
    val body: String,
    val image: String,
    val type: String,
    val date: String,
    val time: String,
    var isRead: Boolean = false
)