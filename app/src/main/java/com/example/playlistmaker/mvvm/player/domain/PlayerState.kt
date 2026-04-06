package com.example.playlistmaker.mvvm.player.domain

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.mvvm.player.domain.PlayingStatus

data class PlayerState(val playingStatus: PlayingStatus, val playingTrack: Track?, var playedTime:String) {
}