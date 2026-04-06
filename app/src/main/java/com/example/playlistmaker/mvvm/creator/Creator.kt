package com.example.playlistmaker.mvvm.creator

import android.content.Context
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.StorageSharedPrefImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.Storage
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private const val BASE_URL = "https://itunes.apple.com/"
    val libraryConverterJson = GsonConverterFactory.create()
    val libraryNetwork = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(libraryConverterJson)
        .build()
    val librarySerializable = Gson()

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    fun provideStorageInteractor(context: Context, key: String): Storage {
        return StorageSharedPrefImpl(context, key)
    }


    fun provideSearchHistoryRepository(context: Context, key: String): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(StorageSharedPrefImpl(context, key))
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(context)
    }



}