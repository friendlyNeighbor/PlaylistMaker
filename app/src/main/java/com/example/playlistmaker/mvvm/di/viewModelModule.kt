package com.example.playlistmaker.mvvm.di

import com.example.playlistmaker.mvvm.media.ui.FavoritesViewModel
import com.example.playlistmaker.mvvm.media.ui.PlaylistsViewModel
import com.example.playlistmaker.mvvm.player.ui.PlayerState
import com.example.playlistmaker.mvvm.player.ui.PlayerViewModel
import com.example.playlistmaker.mvvm.search.ui.SearchState
import com.example.playlistmaker.mvvm.search.ui.SearchViewModel
import com.example.playlistmaker.mvvm.settings.ui.SettingsState
import com.example.playlistmaker.mvvm.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (primaryState: PlayerState) ->
        PlayerViewModel(primaryState, get())
    }

    viewModel { (primaryState: SearchState) ->
        SearchViewModel(primaryState, get(), get())
    }

    viewModel { (primaryState: SettingsState) ->
        SettingsViewModel(primaryState, get(), get())
    }

    viewModel { PlaylistsViewModel() }

    viewModel { FavoritesViewModel() }

}