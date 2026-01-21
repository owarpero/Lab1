package com.example.messengerapp

import android.app.Application
import androidx.work.*
import com.example.messengerapp.notifications.NotificationHelper
import com.example.messengerapp.workers.SyncMessagesWorker
import java.util.concurrent.TimeUnit

class MessengerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createNotificationChannel(this)
        schedulePeriodicSync()
    }

    private fun schedulePeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncMessagesWorker>(
            1, TimeUnit.MINUTES,
            15, TimeUnit.SECONDS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "message_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
}
