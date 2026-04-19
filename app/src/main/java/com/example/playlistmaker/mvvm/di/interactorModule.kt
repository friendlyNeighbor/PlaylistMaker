package com.example.playlistmaker.mvvm.di

import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.mvvm.search.domain.impl.TrackSearchInteractorImpl
import com.example.playlistmaker.mvvm.settings.domain.ThemeInteractorImpl
import com.example.playlistmaker.mvvm.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractor
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module


val interactorModule = module {

    single<TrackSearchInteractor> {
        TrackSearchInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<ThemeInteractor> {
        ThemeInteractorImpl(get(named(DARK_THEME)), get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

}

private const val DARK_THEME = "DARK_THEME"
