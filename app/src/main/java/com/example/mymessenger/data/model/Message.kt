package com.example.mymessenger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("postId")
    val postId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("body")
    val body: String,

    val timestamp: Long = System.currentTimeMillis()
)

