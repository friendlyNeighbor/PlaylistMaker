package com.example.playlistmaker.mvvm.sharing.data

interface SharingRepository {
    fun provideLink():String
    fun provideTextMessage():String
    fun provideSubjectMessage():String
    fun provideEmailAddress():String
    fun provideLinkOffer():String
}