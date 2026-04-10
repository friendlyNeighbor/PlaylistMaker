package com.example.playlistmaker.mvvm.settings.domain.api

interface ThemeInteractor {
    fun getTheme():Boolean
    fun switchTheme()
    fun updateTheme()
}