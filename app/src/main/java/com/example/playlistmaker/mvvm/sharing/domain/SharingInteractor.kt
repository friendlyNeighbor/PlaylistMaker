package com.example.playlistmaker.mvvm.sharing.domain

interface SharingInteractor {
    fun shareApp()
    fun sharePlaylist(message:String)
    fun openTerms()
    fun openSupport()
}