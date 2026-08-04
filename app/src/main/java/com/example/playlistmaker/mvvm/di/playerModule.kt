package com.example.playlistmaker.mvvm.di

import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractorImpl
import org.koin.core.qualifier.named

import org.koin.dsl.module

val playerModule = module {
    factory<TrackSaverInteractor> {
        TrackSaverInteractorImpl(get(named(PLAYER)))
    }
}

private const val PLAYER = "PLAYER"