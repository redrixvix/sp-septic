package com.memoryproject.app.data.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var darkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()

    var displayName: String
        get() = prefs.getString(KEY_DISPLAY_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_DISPLAY_NAME, value).apply()

    fun clearAll() {
        // Clear auth tokens/session data but preserve user preferences
        prefs.edit()
            .remove(KEY_SESSION_COOKIE)
            .remove(KEY_AUTH_TOKEN)
            .apply()
        // Note: darkMode and notificationsEnabled are preserved intentionally
        // so the user's appearance/notification preferences survive logout
    }

    companion object {
        private const val PREFS_NAME = "memory_project_prefs"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_DISPLAY_NAME = "display_name"
        // Auth keys — stored in SharedPreferences (not currently used; session is in-memory)
        private const val KEY_SESSION_COOKIE = "session_cookie"
        private const val KEY_AUTH_TOKEN = "auth_token"
    }
}