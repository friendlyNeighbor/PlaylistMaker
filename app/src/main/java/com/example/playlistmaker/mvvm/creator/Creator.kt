package com.example.playlistmaker.mvvm.creator

import android.content.Context
import com.example.playlistmaker.mvvm.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.mvvm.settings.data.StorageSharedPrefImpl
import com.example.playlistmaker.mvvm.search.data.TrackSearchRepositoryImpl
import com.example.playlistmaker.mvvm.search.data.network.RetrofitClient
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.mvvm.settings.domain.api.Storage
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.mvvm.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.mvvm.search.domain.impl.TrackSearchInteractorImpl
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

    private fun getTrackSearchRepository(): TrackSearchRepository {
        return TrackSearchRepositoryImpl(RetrofitClient())
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackSearchRepository())
    }

    fun provideStorageInteractor(key: String): Storage {
        return StorageSharedPrefImpl(key)
    }


    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(StorageSharedPrefImpl(HISTORY))
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private const val HISTORY = "HISTORY"

}