package com.example.playlistmaker.data

import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.Storage
import com.example.playlistmaker.domain.models.Track
import com.google.gson.reflect.TypeToken
import kotlin.collections.removeAll

class SearchHistoryRepositoryImpl(private val storageSearchHistory: Storage): SearchHistoryRepository {
    private val librarySerializable = Creator.librarySerializable

    override fun addTrackInHistory(track: Track) {
        val trackListHistory = getTrackListHistory()
        trackListHistory.removeAll { it.trackId == track.trackId }
        trackListHistory.add(0, track)
        if (trackListHistory.size > MAX_LENGTH_HISTORY) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
        val trackListHistoryJson = librarySerializable.toJson(trackListHistory)
        storageSearchHistory.save(trackListHistoryJson)
        }

    override fun clearHistory() {
        storageSearchHistory.clear()
    }

    override fun getTrackListHistory(): MutableList<Track> {
        val trackListHistoryJson = storageSearchHistory.get() ?: return mutableListOf()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return librarySerializable.fromJson<List<Track>?>(trackListHistoryJson as String, type).toMutableList()
    }
    companion object {
        private const val MAX_LENGTH_HISTORY = 10
    }
}