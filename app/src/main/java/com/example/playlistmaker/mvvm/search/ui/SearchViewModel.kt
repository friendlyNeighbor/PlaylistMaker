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

    private val _searchLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SearchState> = _searchLiveData

    private var textInFocus = true //
    var text = ""

    private var searchJob: Job? = null

    fun editTextInFocus() {
        textInFocus = true
        textWasChanged("")
    }

    fun textWasChanged(incomingText: String) {
        if (incomingText != text) {
            text = incomingText.trimStart()

            if (textInFocus) {
                val trackListHistory = searchHistoryInteractor.getTrackListHistory()
                if (text.isEmpty()) {
                    searchJob?.cancel()
                    if (trackListHistory.isEmpty()) {
                        _searchLiveData.value = (SearchState(SearchStatus.CLEAR, emptyList()))
                    } else {
                        _searchLiveData.value = (
                            SearchState(
                                SearchStatus.HISTORY,
                                trackListHistory
                            )
                        )
                    }
                } else {
                    _searchLiveData.value = (SearchState(SearchStatus.PROGRESS, emptyList()))
                    debounceSearchTrack()
                }
            }
        }
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        _searchLiveData.value = (SearchState(SearchStatus.CLEAR, emptyList()))
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
            _searchLiveData.value = (SearchState(SearchStatus.CONNECTION_PROBLEM, trackList))
        } else {
            trackList.clear()
            trackList.addAll(foundTrack)
            if (trackList.isEmpty()) {
                _searchLiveData.value = (SearchState(SearchStatus.NOT_FOUND, trackList))
            } else {
                _searchLiveData.value = (
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
