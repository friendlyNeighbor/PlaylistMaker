package com.example.playlistmaker.mvvm.settings.data

import androidx.appcompat.app.AppCompatDelegate

class ThemeSwitcherImpl(): ThemeSwitcher {
    override fun enableLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    override fun enableDarkTheme() {
       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}