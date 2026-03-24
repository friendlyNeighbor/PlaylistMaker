package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.Storage
import androidx.core.content.edit

class StorageSharedPrefImpl(context: Context, val key: String) : Storage {

    val prefs: SharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE)

    override fun save(value: Any?) {
        if (value is Boolean) {
            prefs.edit {
                putBoolean(key, value)
            }
        }
        if (value is String) {
            prefs.edit {
                putString(key, value)
            }
        }
    }

    override fun get(): Any? {
        val all = prefs.all
        val value = all[key]

        return when(value) {
            is Boolean -> prefs.getBoolean(key, false)
            is String -> prefs.getString(key, "")
            else -> null
        }
    }

    override fun clear() {
        prefs.edit {
            clear()
        }
    }
}
