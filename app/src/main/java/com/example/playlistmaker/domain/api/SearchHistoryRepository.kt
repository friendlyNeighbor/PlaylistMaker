package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun addTrackInHistory(track: Track)
    fun clearHistory()
    fun getTrackListHistory(): MutableList<Track>
}