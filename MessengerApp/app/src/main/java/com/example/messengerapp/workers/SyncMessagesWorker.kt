package com.example.messengerapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.messengerapp.data.local.AppDatabase
import com.example.messengerapp.data.remote.RetrofitClient
import com.example.messengerapp.data.repository.MessageRepository
import com.example.messengerapp.data.repository.Resource
import com.example.messengerapp.notifications.NotificationHelper

class SyncMessagesWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("SyncMessagesWorker", "Starting background sync...")

        return try {
            val database = AppDatabase.getDatabase(applicationContext)
            val repository = MessageRepository(
                RetrofitClient.apiService,
                database.messageDao()
            )

            val previousCount = database.messageDao().getMessageCount()

            when (val result = repository.refreshMessages()) {
                is Resource.Success -> {
                    val newMessageCount = repository.getNewMessageCount(previousCount)
                    NotificationHelper.showSyncNotification(applicationContext, newMessageCount)
                    Result.success()
                }
                is Resource.Error -> {
                    Log.e("SyncMessagesWorker", "Sync failed: ${result.message}")
                    Result.retry()
                }
                is Resource.Loading -> Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SyncMessagesWorker", "Exception: ${e.message}", e)
            Result.failure()
        }
    }
}
