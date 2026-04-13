package com.example.playlistmaker.mvvm.search.domain.api

import com.example.playlistmaker.mvvm.search.domain.model.Track

interface SearchHistoryRepository {
    fun addTrackInHistory(track: Track)
    fun clearHistory()
    fun getTrackListHistory(): MutableList<Track>
}