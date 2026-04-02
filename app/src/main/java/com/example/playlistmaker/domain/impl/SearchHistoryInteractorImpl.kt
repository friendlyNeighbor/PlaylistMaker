package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(context: Context): SearchHistoryInteractor {
    val searchHistory = Creator.provideSearchHistoryRepository(context, HISTORY)
    override fun addTrackInHistory(track: Track) {
        searchHistory.addTrackInHistory(track)
    }
    override fun clearHistory() {
        searchHistory.clearHistory()
    }
    override fun getTrackListHistory(): MutableList<Track> {
        return searchHistory.getTrackListHistory()
    }

companion object {
    private const val HISTORY = "HISTORY"
}
}
