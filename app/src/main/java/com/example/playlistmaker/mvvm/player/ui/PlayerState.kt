package com.example.playlistmaker.mvvm.player.ui

import com.example.playlistmaker.mvvm.search.domain.model.Track

data class PlayerState(val playingStatus: PlayingStatus, val playingTrack: Track, var playedTime:String, var isFavoriteTrack:Boolean)