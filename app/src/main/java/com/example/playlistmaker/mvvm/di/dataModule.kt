package com.example.playlistmaker.mvvm.di

import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.mvvm.media.data.db.AppDatabase
import com.example.playlistmaker.mvvm.search.data.network.ITunesApiService
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
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

    factory { Gson() }

    factory { MediaPlayer() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}
