package com.example.messengerapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.messengerapp.data.local.MessageDao
import com.example.messengerapp.data.local.MessageEntity
import com.example.messengerapp.data.remote.ApiService
import com.example.messengerapp.data.remote.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}

class MessageRepository(
    private val apiService: ApiService,
    private val messageDao: MessageDao
) {
    val messages: LiveData<List<MessageEntity>> = messageDao.getAllMessages()

    suspend fun refreshMessages(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d("MessageRepository", "Fetching from API...")
            val response = apiService.getPosts()

            if (response.isSuccessful) {
                response.body()?.let { posts ->
                    val entities = posts.map { it.toEntity() }
                    messageDao.insertMessages(entities)
                    Log.d("MessageRepository", "Saved ${entities.size} messages")
                    Resource.Success(Unit)
                } ?: Resource.Error("Empty response")
            } else {
                Resource.Error("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MessageRepository", "Error: ${e.message}", e)
            Resource.Error("Network error: ${e.localizedMessage}")
        }
    }

    suspend fun hasCachedData(): Boolean = withContext(Dispatchers.IO) {
        messageDao.getMessageCount() > 0
    }
}
