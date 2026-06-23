package com.example.playlistmaker.mvvm.di

import com.example.playlistmaker.mvvm.media.data.db.FavoritesRepositoryImpl
import com.example.playlistmaker.mvvm.media.data.db.PlaylistRepositoryImpl
import com.example.playlistmaker.mvvm.media.data.db.TrackInPlaylistsRepositoryImpl
import com.example.playlistmaker.mvvm.media.data.db.converters.PlaylistDbConvertor
import com.example.playlistmaker.mvvm.media.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.mvvm.media.data.db.converters.TrackInPlaylistsDbConvertor
import com.example.playlistmaker.mvvm.media.data.impl.ImageSaverRepositoryImpl
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverRepository
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesRepository
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistRepository
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

    factory<TrackSearchRepository> {
        TrackSearchRepositoryImpl(get())
    }

    factory<NetworkClient> {
        RetrofitClient(get(), get())
    }

    factory<SearchHistoryRepository>(named(HISTORY)) {
        SearchHistoryRepositoryImpl(get(named(HISTORY)), get())
    }

    factory<SearchHistoryRepository>(named(PLAYER)) {
        SearchHistoryRepositoryImpl(get(named(PLAYER)), get())
    }

    single<Storage>(named(HISTORY)) {
        SharedPrefRepositoryImpl(HISTORY, get())
    }

    factory<ThemeSwitcher> {
        ThemeSwitcherImpl()
    }

    single<Storage>(named(DARK_THEME)) {
        SharedPrefRepositoryImpl(DARK_THEME, get())
    }

    single<Storage>(named(PLAYER)) {
        SharedPrefRepositoryImpl(PLAYER, get())
    }

    factory {
        ExternalNavigator(get())
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(get())
    }

    factory {
        TrackDbConvertor()
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory<ImageSaverRepository> {
        ImageSaverRepositoryImpl(get())
    }

    factory {
        PlaylistDbConvertor()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    factory {
        TrackInPlaylistsDbConvertor()
    }

    single<FavoritesRepository>(named("2")) {
        TrackInPlaylistsRepositoryImpl(get(), get())
    }

}
    private const val HISTORY = "HISTORY"
    private const val DARK_THEME = "DARK_THEME"
    private const val PLAYER = "PLAYER"

