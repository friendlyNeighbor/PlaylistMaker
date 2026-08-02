package com.example.playlistmaker.mvvm.player.services.api

import com.example.playlistmaker.mvvm.player.ui.PlayingStatus
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayingStatus(): StateFlow<PlayingStatus>
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun showNotification()
    fun hideNotification()
}