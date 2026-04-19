package com.example.playlistmaker.mvvm.di

import android.media.MediaPlayer
import com.example.playlistmaker.mvvm.search.data.network.ITunesApiService
import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    factory{ Gson() }

    factory{ MediaPlayer() }
}
