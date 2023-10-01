package com.example.ecommerce.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.repository.NotificationRepository
import com.example.ecommerce.room.entity.NotificationEntity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.quickstart.fcm.kotlin.MyWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var repository: NotificationRepository
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")


            if (isLongRunningJob()) {
                scheduleJob()
            } else {
                handleNow()
            }
        }

        remoteMessage.data?.let {
            Log.d(TAG, "Message Notification Body: ${it}")
            it?.let { body -> sendNotification(body) }
            it?.let { body -> saveToRoom(body) }
        }
    }

    private fun saveToRoom(body: Map<String, String>) {
        Log.d(TAG, "saveToRoom")
        val notification = NotificationEntity(
            title = body["title"].toString(),
            body = body["body"].toString(),
            image = body["image"].toString(),
            type = body["type"].toString(),
            date = body["date"].toString(),
            time = body["time"].toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.insertToNotification(notification)
        }

    }

    private fun isLongRunningJob() = true
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
        // [END dispatch_job]
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    fun sendNotification(messageBody: MutableMap<String, String>) {

        val requestCode = 0

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.app_navigation)
            .setDestination(R.id.notificationFragment)
            .createPendingIntent()

        val channelId = "channelId"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.image_thumbnail)
            .setContentTitle(messageBody["title"])
            .setContentText(messageBody["body"])
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}