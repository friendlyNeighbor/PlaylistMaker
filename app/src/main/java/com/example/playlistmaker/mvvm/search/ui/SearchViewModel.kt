package com.example.playlistmaker.mvvm.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    primaryState: SearchState,
    private val trackSearchInteractor: TrackSearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackSaverInteractor: TrackSaverInteractor
) : ViewModel() {

    private val searchLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SearchState> = searchLiveData

    private var textInFocus = false
    private var text = ""

    private var searchJob: Job? = null

    fun editTextInFocus() {
        textInFocus = true
        textWasChanged("")
    }

    fun textWasChanged(incomingText: String) {
        text = incomingText.trimStart()

        if (textInFocus) {
            val trackListHistory = searchHistoryInteractor.getTrackListHistory()
            if (text.isEmpty()) {
                searchJob?.cancel()
                if (trackListHistory.isEmpty()) {
                    searchLiveData.postValue(SearchState(SearchStatus.CLEAR, emptyList()))
                } else {
                    searchLiveData.postValue(SearchState(SearchStatus.HISTORY, trackListHistory))
                }
            } else {
                searchLiveData.postValue(SearchState(SearchStatus.PROGRESS, emptyList()))
                debounceSearchTrack()
            }
        }
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        searchLiveData.postValue(SearchState(SearchStatus.CLEAR, emptyList()))
    }

    private fun debounceSearchTrack() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack()
        }
    }

private suspend fun searchTrack() {
    trackSearchInteractor.searchTrack(text)
        .collect { pair ->
            processResult(pair.first, pair.second)
        }
}
    private fun processResult(foundTrack: List<Track>?, errorMessage: String?) {
        val trackList: MutableList<Track> = mutableListOf()
        if (errorMessage != null || foundTrack == null) {
            searchLiveData.postValue(SearchState(SearchStatus.CONNECTION_PROBLEM, trackList))
        } else {
            trackList.clear()
            trackList.addAll(foundTrack)
            if (trackList.isEmpty()) {
                searchLiveData.postValue(SearchState(SearchStatus.NOT_FOUND, trackList))
            } else {
                searchLiveData.postValue(
                    SearchState(
                        SearchStatus.SEARCH_SUCCESSFUL,
                        trackList
                    )
                )
            }
        }
    }

    fun addTrackInHistory(track: Track) {
        searchHistoryInteractor.addTrackInHistory(track)
    }

    fun addTrackInMemory(track: Track) {
        trackSaverInteractor.addTrackInMemory(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
