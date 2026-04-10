package com.example.playlistmaker.mvvm.settings.domain

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.mvvm.settings.domain.api.Storage
import com.example.playlistmaker.mvvm.settings.domain.api.ThemeInteractor

class ThemeInteractorImpl(val themeStorage: Storage): ThemeInteractor {

    override fun getTheme():Boolean {

        return themeStorage.getValue() as Boolean
    }

    override fun updateTheme() {
        val isDarkMode = themeStorage.getValue() as Boolean

        if (isDarkMode) {
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        if (!isDarkMode) {
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun switchTheme() {
            themeStorage.save(!(themeStorage.getValue() as Boolean))
            updateTheme()
        }
}