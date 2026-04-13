package com.example.playlistmaker.mvvm.search.domain.impl

import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track

class SearchHistoryInteractorImpl(val searchHistoryRepository: SearchHistoryRepository): SearchHistoryInteractor {

    override fun addTrackInHistory(track: Track) {
        searchHistoryRepository.addTrackInHistory(track)
    }
    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
    override fun getTrackListHistory(): MutableList<Track> {
        return searchHistoryRepository.getTrackListHistory()
    }

}
