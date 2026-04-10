package com.example.playlistmaker.mvvm

import android.annotation.SuppressLint
import android.app.Application
import com.example.playlistmaker.mvvm.creator.Creator

class App : Application() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()
        instance = this
        val themeInteractor = Creator.provideThemeInteractor()
        themeInteractor.updateTheme()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}