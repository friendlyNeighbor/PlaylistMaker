package com.example.playlistmaker.mvvm.settings.domain.api

interface Storage {
    fun save(value: Any?)
    fun clear()
    fun get():Any?
}