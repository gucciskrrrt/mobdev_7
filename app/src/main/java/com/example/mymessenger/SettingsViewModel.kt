package com.example.mymessenger

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _isDarkTheme = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    init {
        Log.d("SettingsViewModel", "ViewModel created")
    }

    fun setDarkTheme(isDark: Boolean) {
        Log.d("SettingsViewModel", "setDarkTheme: $isDark")
        _isDarkTheme.value = isDark
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SettingsViewModel", "ViewModel destroyed")
    }
}

