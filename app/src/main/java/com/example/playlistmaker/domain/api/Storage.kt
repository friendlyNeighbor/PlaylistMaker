package com.example.playlistmaker.domain.api

interface Storage {
    fun save(value: Any?)
    fun clear()
    fun get():Any?
}