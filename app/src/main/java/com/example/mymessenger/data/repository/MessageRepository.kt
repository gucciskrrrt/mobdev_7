package com.example.mymessenger.data.repository

import android.content.Context
import android.util.Log
import com.example.mymessenger.data.local.MessageDao
import com.example.mymessenger.data.model.Message
import com.example.mymessenger.data.remote.ApiService
import com.example.mymessenger.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepository(
    private val messageDao: MessageDao,
    private val apiService: ApiService,
    private val context: Context
) {

    companion object {
        private const val TAG = "MessageRepository"
    }

    // Получаем сообщения из локальной базы как Flow
    fun getMessagesFromDatabase(): Flow<List<Message>> {
        return messageDao.getAllMessages()
    }

    // Загружаем сообщения с API и сохраняем в базу
    suspend fun refreshMessages(): Resource<List<Message>> {
        return withContext(Dispatchers.IO) {
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    Log.d(TAG, "No internet connection")
                    return@withContext Resource.Error("Нет подключения к интернету")
                }

                Log.d(TAG, "Fetching messages from API...")
                val messages = apiService.getMessages(limit = 50)
                Log.d(TAG, "Received ${messages.size} messages from API")

                // Сохраняем в базу
                messageDao.insertMessages(messages)
                Log.d(TAG, "Messages saved to database")

                Resource.Success(messages)
            } catch (e: Exception) {
                Log.e(TAG, "Error refreshing messages", e)
                Resource.Error("Ошибка загрузки: ${e.localizedMessage}")
            }
        }
    }

    // Получаем количество сообщений в базе
    suspend fun getMessagesCount(): Int {
        return withContext(Dispatchers.IO) {
            messageDao.getMessagesCount()
        }
    }

    // Очищаем базу данных
    suspend fun clearMessages() {
        withContext(Dispatchers.IO) {
            messageDao.deleteAllMessages()
        }
    }

    // Получаем сообщение по ID
    suspend fun getMessageById(id: Int): Message? {
        return withContext(Dispatchers.IO) {
            messageDao.getMessageById(id)
        }
    }
}

