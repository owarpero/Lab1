package com.example.messengerapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.messengerapp.data.local.MessageDao
import com.example.messengerapp.data.local.MessageEntity
import com.example.messengerapp.data.local.UserData
import com.example.messengerapp.data.remote.ApiService
import com.example.messengerapp.data.remote.toEntity
import com.example.messengerapp.data.remote.toUserData
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

    // User data cache
    private val userCache = mutableMapOf<Int, UserData>()

    suspend fun refreshMessages(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d("MessageRepository", "Fetching from API...")
            val response = apiService.getPosts()

            if (response.isSuccessful) {
                response.body()?.let { posts ->
                    val entities = posts.map { it.toEntity() }

                    // Fetch user data
                    val uniqueUserIds = entities.map { it.userId }.distinct()
                    fetchAndCacheUsers(uniqueUserIds)

                    // Update with user data
                    val entitiesWithUsers = entities.map { entity ->
                        entity.copy(userData = userCache[entity.userId])
                    }

                    messageDao.insertMessages(entitiesWithUsers)
                    Log.d("MessageRepository", "Saved ${entitiesWithUsers.size} messages")
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

    private suspend fun fetchAndCacheUsers(userIds: List<Int>) {
        userIds.forEach { userId ->
            if (!userCache.containsKey(userId)) {
                try {
                    val userResponse = apiService.getUser(userId)
                    if (userResponse.isSuccessful) {
                        userResponse.body()?.let { user ->
                            userCache[userId] = user.toUserData()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MessageRepository", "Failed to fetch user $userId")
                }
            }
        }
    }

    suspend fun toggleLike(messageId: Int): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val message = messageDao.getMessageById(messageId)
            if (message != null) {
                val newLikeStatus = !message.isLiked
                messageDao.updateMessageLiked(messageId, newLikeStatus)
                Resource.Success(Unit)
            } else {
                Resource.Error("Message not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to toggle like")
        }
    }

    suspend fun getNewMessageCount(previousCount: Int): Int = withContext(Dispatchers.IO) {
        maxOf(0, messageDao.getMessageCount() - previousCount)
    }

    suspend fun hasCachedData(): Boolean = withContext(Dispatchers.IO) {
        messageDao.getMessageCount() > 0
    }
}
