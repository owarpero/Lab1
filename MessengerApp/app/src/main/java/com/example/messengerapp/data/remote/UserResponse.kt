package com.example.messengerapp.data.remote

import com.example.messengerapp.data.local.UserData

data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)

fun UserResponse.toUserData() = UserData(
    name = name,
    email = email
)
