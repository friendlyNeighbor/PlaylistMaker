package com.example.playlistmaker.data

import android.content.Context
import com.example.playlistmaker.domain.api.Storage

class StorageSharedPrefImpl(context: Context, val key: String) : Storage {

    val prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE)

    override fun save(value: Any?) {
        if (value is Boolean) {
            prefs.edit()
                .putBoolean(key, value)
                .apply()
        }
        if (value is String) {
            prefs.edit()
                .putString(key, value)
                .apply()
        }
    }

    override fun get(): Any? {
        val all = prefs.all
        val value = all[key]

        if (value is Boolean)
            return prefs.getBoolean(key, false)
        if (value is String)
            return prefs.getString(key, " ")
        else
            return null
    }

    override fun clear() {
        prefs.edit()
            .clear()
            .apply()
    }
}
