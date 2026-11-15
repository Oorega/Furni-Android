package com.example.furni.util

import android.content.Context

class SessionManager(private val context: Context) {
    private val prefsName = "furni_prefs"
    private val keyUserId = "logged_in_user_id"

    fun getUserId(): Long? {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return if (prefs.contains(keyUserId)) prefs.getLong(keyUserId, -1L) else null
    }
}