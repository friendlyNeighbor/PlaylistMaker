package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    lateinit var shrdPrefNightMode: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        shrdPrefNightMode = getSharedPreferences(SP_SWITCHER_THEME, MODE_PRIVATE)
        var darkTheme = shrdPrefNightMode.getBoolean(SP_SWITCHER_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        shrdPrefNightMode.edit()
            .putBoolean(SP_SWITCHER_THEME_KEY, darkThemeEnabled)
            .apply()
    }

    companion object {
        val SP_SWITCHER_THEME = "SP_SWITCHER_THEME"
        val SP_SWITCHER_THEME_KEY = "SP_SWITCHER_THEME_KEY"
    }
}