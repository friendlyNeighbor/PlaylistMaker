package com.example.playlistmaker.mvvm.di

import com.example.playlistmaker.mvvm.search.data.NetworkClient
import com.example.playlistmaker.mvvm.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.mvvm.search.data.TrackSearchRepositoryImpl
import com.example.playlistmaker.mvvm.search.data.network.RetrofitClient
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.mvvm.settings.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.mvvm.settings.data.Storage
import com.example.playlistmaker.mvvm.settings.data.ThemeSwitcher
import com.example.playlistmaker.mvvm.settings.data.ThemeSwitcherImpl
import com.example.playlistmaker.mvvm.sharing.data.ExternalNavigator
import com.example.playlistmaker.mvvm.sharing.data.SharingRepository
import com.example.playlistmaker.mvvm.sharing.data.SharingRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module


val repositoryModule = module {

    single<TrackSearchRepository> {
        TrackSearchRepositoryImpl(get())
    }

    factory<NetworkClient> {
        RetrofitClient(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named(HISTORY)), get())
    }

    single<Storage>(named(HISTORY)) {
        SharedPrefRepositoryImpl(HISTORY, get())
    }

    single<ThemeSwitcher> {
        ThemeSwitcherImpl()
    }

    single<Storage>(named(DARK_THEME)) {
        SharedPrefRepositoryImpl(DARK_THEME, get())
    }

    single {
        ExternalNavigator(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get())
    }

}

private const val HISTORY = "HISTORY"
private const val DARK_THEME = "DARK_THEME"


