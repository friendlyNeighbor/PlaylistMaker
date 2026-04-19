package com.example.playlistmaker.mvvm.creator

/*
import android.content.Context
import com.example.playlistmaker.mvvm.App
import com.example.playlistmaker.mvvm.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.mvvm.settings.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.mvvm.search.data.TrackSearchRepositoryImpl
import com.example.playlistmaker.mvvm.search.data.network.ITunesApiService
import com.example.playlistmaker.mvvm.search.data.network.RetrofitClient
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.mvvm.settings.data.Storage
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.mvvm.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.mvvm.search.domain.impl.TrackSearchInteractorImpl
import com.example.playlistmaker.mvvm.settings.data.ThemeSwitcherImpl
import com.example.playlistmaker.mvvm.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.mvvm.settings.domain.ThemeInteractorImpl
import com.example.playlistmaker.mvvm.sharing.data.ExternalNavigator
import com.example.playlistmaker.mvvm.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractor
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractorImpl
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
*/
object Creator {
/*
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

    private fun getSharedPrefRepository(key: String): Storage {
        return SharedPrefRepositoryImpl(key)
    }
    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getSharedPrefRepository(DARK_THEME), ThemeSwitcherImpl())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(getSharedPrefRepository(HISTORY))
    }
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(ExternalNavigator(), SharingRepositoryImpl())
    }

    private const val HISTORY = "HISTORY"
    private const val DARK_THEME = "DARK_THEME"


     */
}