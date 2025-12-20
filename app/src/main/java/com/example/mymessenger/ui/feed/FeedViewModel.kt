package com.example.mymessenger.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymessenger.data.local.AppDatabase
import com.example.mymessenger.data.model.Message
import com.example.mymessenger.data.remote.RetrofitClient
import com.example.mymessenger.data.repository.MessageRepository
import com.example.mymessenger.data.repository.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MessageRepository

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    init {
        val database = AppDatabase.getDatabase(application)
        val messageDao = database.messageDao()
        val apiService = RetrofitClient.apiService

        repository = MessageRepository(messageDao, apiService, application)

        // Подписываемся на изменения в базе данных
        observeMessages()

        // Загружаем данные при инициализации
        refreshMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            repository.getMessagesFromDatabase().collectLatest { messageList ->
                _messages.value = messageList
            }
        }
    }

    fun refreshMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            when (val result = repository.refreshMessages()) {
                is Resource.Success -> {
                    _successMessage.value = "Сообщения обновлены (${result.data?.size ?: 0})"
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message

                    // Проверяем, есть ли данные в локальной базе
                    val count = repository.getMessagesCount()
                    if (count > 0) {
                        _successMessage.value = "Загружены данные из кэша ($count)"
                    }
                }
                is Resource.Loading -> {
                    // Обрабатывается через _isLoading
                }
            }

            _isLoading.value = false
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }
}

