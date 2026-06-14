package com.example.playlistmaker.mvvm.player.domain

import com.example.playlistmaker.mvvm.search.domain.model.Track

interface TrackSaverInteractor {
    fun addTrackInMemory(track: Track)
    fun clearMemory()
    fun getTrackListMemory(): MutableList<Track>
}