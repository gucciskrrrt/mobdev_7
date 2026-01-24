package com.example.mymessenger.data.repository

import android.content.Context
import android.util.Log
import com.example.mymessenger.data.local.MessageDao
import com.example.mymessenger.data.model.Message
import com.example.mymessenger.data.remote.ApiService
import com.example.mymessenger.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
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
                    Log.d(TAG, "No internet connection, returning cached data")
                    val cachedMessages = messageDao.getAllMessagesOnce()
                    return@withContext if (cachedMessages.isNotEmpty()) {
                        Resource.Error("Нет подключения к интернету. Показаны данные из кэша.", cachedMessages)
                    } else {
                        Resource.Error("Нет подключения к интернету")
                    }
                }

                Log.d(TAG, "Fetching messages from API...")
                val messages = apiService.getMessages(limit = 50)
                Log.d(TAG, "Received ${messages.size} messages from API")

                // Фильтруем сообщения с валидными данными
                val validMessages = messages.filter {
                    it.name != null && it.email != null && it.body != null
                }

                if (validMessages.isEmpty()) {
                    Log.d(TAG, "No valid messages received from API")
                    val cachedMessages = messageDao.getAllMessagesOnce()
                    return@withContext if (cachedMessages.isNotEmpty()) {
                        Resource.Error("Некорректные данные от сервера. Показаны данные из кэша.", cachedMessages)
                    } else {
                        Resource.Error("Некорректные данные от сервера")
                    }
                }

                // Сохраняем в базу, сохраняя статус лайков
                val existingMessages = messageDao.getAllMessagesOnce()
                val likedIds = existingMessages.filter { it.isLiked }.map { it.id }.toSet()

                val messagesWithLikes = validMessages.map { message ->
                    if (likedIds.contains(message.id)) {
                        message.copy(isLiked = true)
                    } else {
                        message
                    }
                }

                messageDao.insertMessages(messagesWithLikes)
                Log.d(TAG, "Messages saved to database (preserved ${likedIds.size} likes)")

                Resource.Success(validMessages)
            } catch (e: java.net.UnknownHostException) {
                Log.e(TAG, "DNS resolution failed - likely VPN/network issue", e)
                val cachedMessages = messageDao.getAllMessagesOnce()
                if (cachedMessages.isNotEmpty()) {
                    Resource.Error("Проблема с сетью. Показаны данные из кэша.", cachedMessages)
                } else {
                    Resource.Error("Не удается подключиться к серверу. Проверьте VPN или сеть.")
                }
            } catch (e: java.net.ConnectException) {
                Log.e(TAG, "Connection failed", e)
                val cachedMessages = messageDao.getAllMessagesOnce()
                if (cachedMessages.isNotEmpty()) {
                    Resource.Error("Ошибка подключения. Показаны данные из кэша.", cachedMessages)
                } else {
                    Resource.Error("Не удается подключиться к серверу")
                }
            } catch (e: java.net.SocketTimeoutException) {
                Log.e(TAG, "Connection timeout", e)
                val cachedMessages = messageDao.getAllMessagesOnce()
                if (cachedMessages.isNotEmpty()) {
                    Resource.Error("Превышено время ожидания. Показаны данные из кэша.", cachedMessages)
                } else {
                    Resource.Error("Превышено время ожидания соединения")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error refreshing messages", e)
                val cachedMessages = messageDao.getAllMessagesOnce()
                if (cachedMessages.isNotEmpty()) {
                    Resource.Error("Ошибка: ${e.localizedMessage}. Показаны данные из кэша.", cachedMessages)
                } else {
                    Resource.Error("Ошибка загрузки: ${e.localizedMessage}")
                }
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

    // Переключаем статус лайка
    suspend fun toggleLike(message: Message) {
        withContext(Dispatchers.IO) {
            messageDao.updateLikeStatus(message.id, !message.isLiked)
        }
    }
}

