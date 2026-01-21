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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("UPDATE messages SET isLiked = :isLiked WHERE id = :messageId")
    suspend fun updateMessageLiked(messageId: Int, isLiked: Boolean)

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: Int): MessageEntity?

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}
