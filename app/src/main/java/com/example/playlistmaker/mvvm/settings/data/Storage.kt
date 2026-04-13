package com.example.playlistmaker.mvvm.settings.data

interface Storage {
    fun save(value: Any?)
    fun clear()
    fun getValue():Any?
}