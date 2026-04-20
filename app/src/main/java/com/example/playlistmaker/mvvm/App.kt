package com.example.playlistmaker.mvvm

import android.annotation.SuppressLint
import android.app.Application
import com.example.playlistmaker.mvvm.di.dataModule
import com.example.playlistmaker.mvvm.di.interactorModule
import com.example.playlistmaker.mvvm.di.repositoryModule
import com.example.playlistmaker.mvvm.di.viewModelModule
import com.example.playlistmaker.mvvm.settings.domain.api.ThemeInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App() : Application() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(viewModelModule, repositoryModule, interactorModule, dataModule)
        }

        val themeInteractor: ThemeInteractor by inject()
        themeInteractor.updateTheme()
    }

}
