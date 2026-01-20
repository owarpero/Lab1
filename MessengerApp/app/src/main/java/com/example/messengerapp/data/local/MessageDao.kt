package com.example.messengerapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY id DESC")
    fun getAllMessages(): LiveData<List<MessageEntity>>

    @Query("SELECT COUNT(*) FROM messages")
    suspend fun getMessageCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}
