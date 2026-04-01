package com.example.playlistmaker.domain

import android.content.Context
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.removeAll

class SearchHistory(context: Context) {
    private val gson = Gson()
    val storageTrack = Creator.provideStorageInteractor(context, TRACK_HISTORY)

    fun addTrackInHistory(track: Track) {
        val trackListHistory = getTrackListHistory()
        trackListHistory.removeAll { it.trackId == track.trackId }
        trackListHistory.add(0, track)
        if (trackListHistory.size > MAX_LENGTH_HISTORY) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
        val trackListHistoryJson = gson.toJson(trackListHistory)
        storageTrack.save(trackListHistoryJson)
        }

    fun clearHistory() {
        storageTrack.clear()
    }

    fun getTrackListHistory(): MutableList<Track> {
        val trackListHistoryJson = storageTrack.get() ?: return mutableListOf()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson<List<Track>?>(trackListHistoryJson as String, type).toMutableList()
    }
    companion object {
        private const val MAX_LENGTH_HISTORY = 10
        private const val TRACK_HISTORY = "TRACK_HISTORY"
    }
}