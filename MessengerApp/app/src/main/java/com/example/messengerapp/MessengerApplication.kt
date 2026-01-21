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

        // Минимальный интервал для PeriodicWorkRequest - 15 минут
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncMessagesWorker>(
            15, TimeUnit.MINUTES
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
