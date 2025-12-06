package com.example.mymessenger

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>().apply {
        value = "Иван Иванов"
    }
    val userName: LiveData<String> = _userName

    private val _userStatus = MutableLiveData<String>().apply {
        value = "В сети"
    }
    val userStatus: LiveData<String> = _userStatus

    init {
        Log.d("ProfileViewModel", "ViewModel created")
    }

    fun updateUserName(name: String) {
        Log.d("ProfileViewModel", "updateUserName: $name")
        _userName.value = name
    }

    fun updateUserStatus(status: String) {
        Log.d("ProfileViewModel", "updateUserStatus: $status")
        _userStatus.value = status
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ProfileViewModel", "ViewModel destroyed")
    }
}

