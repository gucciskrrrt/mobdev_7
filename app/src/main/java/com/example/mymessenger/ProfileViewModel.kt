package com.example.mymessenger

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymessenger.utils.PreferencesManager

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)

    private val _userName = MutableLiveData<String>().apply {
        value = preferencesManager.userName
    }
    val userName: LiveData<String> = _userName

    private val _userStatus = MutableLiveData<String>().apply {
        value = preferencesManager.userStatus
    }
    val userStatus: LiveData<String> = _userStatus

    private val _userEmail = MutableLiveData<String>().apply {
        value = preferencesManager.userEmail
    }
    val userEmail: LiveData<String> = _userEmail

    init {
        Log.d("ProfileViewModel", "ViewModel created")
    }

    fun updateUserName(name: String) {
        Log.d("ProfileViewModel", "updateUserName: $name")
        preferencesManager.userName = name
        _userName.value = name
    }

    fun updateUserStatus(status: String) {
        Log.d("ProfileViewModel", "updateUserStatus: $status")
        preferencesManager.userStatus = status
        _userStatus.value = status
    }

    fun updateUserEmail(email: String) {
        Log.d("ProfileViewModel", "updateUserEmail: $email")
        preferencesManager.userEmail = email
        _userEmail.value = email
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ProfileViewModel", "ViewModel destroyed")
    }
}
