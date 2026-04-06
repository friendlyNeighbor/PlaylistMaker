package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.mvvm.creator.Creator

class App : Application() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()
        instance = this
        val storageTheme = Creator.provideStorageInteractor(applicationContext, DARK_THEME)
        val themeIsDark=storageTheme.get()
        if(themeIsDark!=null && themeIsDark as Boolean)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    companion object {
        const val DARK_THEME = "DARK_THEME"
        lateinit var instance: App
            private set
    }
}