package com.example.mymessenger

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymessenger.utils.PreferencesManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)

    private val _isDarkTheme = MutableLiveData<Boolean>().apply {
        value = preferencesManager.isDarkTheme
    }
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    init {
        Log.d("SettingsViewModel", "ViewModel created, isDarkTheme: ${preferencesManager.isDarkTheme}")
    }

    fun setDarkTheme(isDark: Boolean) {
        Log.d("SettingsViewModel", "setDarkTheme: $isDark")
        preferencesManager.isDarkTheme = isDark
        _isDarkTheme.value = isDark
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SettingsViewModel", "ViewModel destroyed")
    }
}
