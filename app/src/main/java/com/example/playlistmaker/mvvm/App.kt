package com.example.playlistmaker.mvvm

import android.annotation.SuppressLint
import android.app.Application
import com.example.playlistmaker.mvvm.creator.Creator
import com.example.playlistmaker.mvvm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()
        instance = this
        val themeInteractor = Creator.provideThemeInteractor()
        themeInteractor.updateTheme()


        startKoin {
            //    androidContext(this@App)
            modules(viewModelModule)
        }

    }

    companion object {
        lateinit var instance: App
            private set
    }
}