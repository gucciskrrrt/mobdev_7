package com.example.mymessenger.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "min_preferences"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_STATUS = "user_status"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_DARK_THEME = "dark_theme"

        private const val DEFAULT_NAME = "Иван Иванов"
        private const val DEFAULT_STATUS = "В сети"
        private const val DEFAULT_EMAIL = "ivan.ivanov@example.com"
    }

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    var userStatus: String
        get() = prefs.getString(KEY_USER_STATUS, DEFAULT_STATUS) ?: DEFAULT_STATUS
        set(value) = prefs.edit().putString(KEY_USER_STATUS, value).apply()

    var userEmail: String
        get() = prefs.getString(KEY_USER_EMAIL, DEFAULT_EMAIL) ?: DEFAULT_EMAIL
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var isDarkTheme: Boolean
        get() = prefs.getBoolean(KEY_DARK_THEME, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_THEME, value).apply()
}
