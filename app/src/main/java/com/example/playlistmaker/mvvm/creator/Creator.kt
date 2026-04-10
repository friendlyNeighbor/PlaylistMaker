package com.example.playlistmaker.mvvm.creator

import android.content.Context
import com.example.playlistmaker.mvvm.App
import com.example.playlistmaker.mvvm.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.mvvm.settings.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.mvvm.search.data.TrackSearchRepositoryImpl
import com.example.playlistmaker.mvvm.search.data.network.RetrofitClient
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.mvvm.settings.domain.api.Storage
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.mvvm.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.mvvm.search.domain.impl.TrackSearchInteractorImpl
import com.example.playlistmaker.mvvm.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.mvvm.settings.domain.ThemeInteractorImpl
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

    fun getAppContext(): Context {
        return App.instance.applicationContext
    }

    private fun getTrackSearchRepository(): TrackSearchRepository {
        return TrackSearchRepositoryImpl(RetrofitClient())
    }
    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackSearchRepository())
    }

    fun getSharedPrefRepository(key: String): Storage {
        return SharedPrefRepositoryImpl(key)
    }
    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getSharedPrefRepository(DARK_THEME))
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(SharedPrefRepositoryImpl(HISTORY))
    }
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private const val HISTORY = "HISTORY"
    private const val DARK_THEME = "DARK_THEME"

}