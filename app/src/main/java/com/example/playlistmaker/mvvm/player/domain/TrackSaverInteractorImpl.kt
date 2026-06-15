package com.example.playlistmaker.mvvm.player.domain

import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track

class TrackSaverInteractorImpl(val searchHistoryRepository: SearchHistoryRepository): TrackSaverInteractor {
    override fun addTrackInMemory(track: Track) {
        searchHistoryRepository.addTrackInHistory(track)
    }
    override fun clearMemory() {
        searchHistoryRepository.clearHistory()
    }
    override fun getTrackFromMemory(): Track {
        val list = searchHistoryRepository.getTrackListHistory()
        val track:Track = list[0]
        return track
    }
}