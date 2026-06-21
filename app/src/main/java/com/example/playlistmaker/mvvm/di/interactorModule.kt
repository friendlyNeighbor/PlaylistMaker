package com.example.playlistmaker.mvvm.di

import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.mvvm.media.domain.impl.ImageSaverInteractorImpl
import com.example.playlistmaker.mvvm.media.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractorImpl
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

    factory<TrackSearchInteractor> {
        TrackSearchInteractorImpl(get())
    }

    factory<TrackSaverInteractor> {
        TrackSaverInteractorImpl(get(named(PLAYER)))
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get(named(HISTORY)))
    }

    factory<ThemeInteractor> {
        ThemeInteractorImpl(get(named(DARK_THEME)), get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory<ImageSaverInteractor> {
        ImageSaverInteractorImpl(get())
    }

    single< PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}

private const val DARK_THEME = "DARK_THEME"
private const val HISTORY = "HISTORY"
private const val PLAYER = "PLAYER"
