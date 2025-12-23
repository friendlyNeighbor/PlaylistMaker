package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreference: SharedPreferences) {

    private val gson = Gson()

    fun addTrackInHistory(track: Track) {
        val trackListHistory = getTrackListHistory().toMutableList()
        trackListHistory.removeAll { it.trackId == track.trackId }
        trackListHistory.add(0, track)
        if (trackListHistory.size > MAX_LENGTH_HISTORY) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
        val trackListHistoryJson = gson.toJson(trackListHistory)
        sharedPreference.edit()
            .putString(KEY_SP_SEARCH_HISTORY, trackListHistoryJson)
            .apply()
    }

    fun clearHistory() {
        sharedPreference.edit()
            .remove(KEY_SP_SEARCH_HISTORY)
            .apply()
    }

    fun getTrackListHistory(): MutableList<Track> {
        val trackListHistoryJson = sharedPreference.getString(KEY_SP_SEARCH_HISTORY, null) ?: return mutableListOf()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson<List<Track>?>(trackListHistoryJson, type).toMutableList()
    }

    companion object {
        private const val KEY_SP_SEARCH_HISTORY = "KEY_SP_SEARCH_HISTORY"
        private const val MAX_LENGTH_HISTORY = 10
    }

}