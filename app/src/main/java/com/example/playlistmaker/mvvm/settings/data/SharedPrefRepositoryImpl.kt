package com.example.playlistmaker.mvvm.settings.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class SharedPrefRepositoryImpl(val key: String, val context: Context) : Storage {

    val prefs: SharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)

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

    override fun getValue(): Any? {
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