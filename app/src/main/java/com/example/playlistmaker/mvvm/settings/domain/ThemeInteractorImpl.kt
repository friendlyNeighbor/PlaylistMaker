package com.example.playlistmaker.mvvm.settings.domain

import com.example.playlistmaker.mvvm.settings.data.ThemeSwitcher
import com.example.playlistmaker.mvvm.settings.data.Storage
import com.example.playlistmaker.mvvm.settings.domain.api.ThemeInteractor

class ThemeInteractorImpl(private val themeStorage: Storage, private val themeSwitcher: ThemeSwitcher): ThemeInteractor {

    override fun getTheme():Boolean =
        if(themeStorage.getValue() !is Boolean)
            false
        else
            themeStorage.getValue() as Boolean


    override fun updateTheme() {
        val isDarkMode=getTheme()

        if (isDarkMode) {
            themeSwitcher.enableDarkTheme()
        }
        if (!isDarkMode) {
            themeSwitcher.enableLightTheme()
        }
    }

    override fun switchTheme() {
            themeStorage.save(!(getTheme()))
            updateTheme()
        }
}