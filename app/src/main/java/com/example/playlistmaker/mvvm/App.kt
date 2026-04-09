package com.example.playlistmaker.mvvm

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.mvvm.creator.Creator

class App : Application() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()
        updateTheme()
    }

    fun updateTheme() {
        instance = this
        val storageThemeInteractor = Creator.provideStorageInteractor(DARK_THEME)
        val themeIsDark=storageThemeInteractor.get()
        val isDarkMode = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES

        if(themeIsDark!=null && themeIsDark as Boolean && !isDarkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }


    companion object {
        const val DARK_THEME = "DARK_THEME"
        lateinit var instance: App
            private set
    }
}
