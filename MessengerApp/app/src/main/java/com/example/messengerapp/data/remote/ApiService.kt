package com.example.messengerapp.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): Response<List<PostResponse>>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<UserResponse>
}
