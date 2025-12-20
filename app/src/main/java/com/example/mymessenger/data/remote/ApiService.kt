package com.example.mymessenger.data.remote

import com.example.mymessenger.data.model.Message
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("comments")
    suspend fun getMessages(
        @Query("_limit") limit: Int = 50
    ): List<Message>

    @GET("comments/{id}")
    suspend fun getMessageById(
        @Path("id") id: Int
    ): Message

    @GET("posts/{postId}/comments")
    suspend fun getMessagesByPostId(
        @Path("postId") postId: Int
    ): List<Message>
}

