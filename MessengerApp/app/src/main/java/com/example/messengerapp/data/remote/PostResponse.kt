package com.example.messengerapp.data.remote

import com.example.messengerapp.data.local.MessageEntity

data class PostResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

fun PostResponse.toEntity() = MessageEntity(
    id = id,
    userId = userId,
    title = title,
    body = body,
    timestamp = System.currentTimeMillis(),
    isLiked = false,
    userData = null
)
