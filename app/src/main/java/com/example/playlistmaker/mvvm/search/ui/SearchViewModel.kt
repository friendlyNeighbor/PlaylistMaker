package com.example.playlistmaker.mvvm.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel (
    primaryState: SearchState,
    val trackSearchInteractor: TrackSearchInteractor,
    val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel() {

    private val searchLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SearchState> = searchLiveData

    private var textInFocus = false
    private var text = ""

    private var searchJob: Job? = null

    fun editTextInFocus() {
        textInFocus = true
        textWasChanged("")
    }

    fun textWasChanged(incomingText:String) {
        text = incomingText.trimStart()

        if(textInFocus) {
            val trackListHistory=searchHistoryInteractor.getTrackListHistory()
            if (text.isEmpty()) {
                searchJob?.cancel()
                if(trackListHistory.isEmpty()) {
                    searchLiveData.postValue(SearchState(SearchStatus.CLEAR, emptyList()))
                }
                else {
                   searchLiveData.postValue(SearchState(SearchStatus.HISTORY, trackListHistory))
                }
            }
            else {
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
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack()
        }
    }
/*
    private fun searchTrack() {
        val trackList: MutableList<Track> = mutableListOf()
        trackSearchInteractor.searchTrack(
            text,
            object : TrackSearchInteractor.TrackConsumer {
                override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                    viewModelScope.launch {
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
                }
            })
    }
    */
private fun searchTrack() {
    //val trackList: MutableList<Track> = mutableListOf()
   // trackSearchInteractor.searchTrack(
     //   text
     //   ,
     //   object : TrackSearchInteractor.TrackConsumer {
     //       override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                viewModelScope.launch {
                    trackSearchInteractor.searchTrack(text)
                        .collect { pair -> processResult(pair.first, pair.second) }
        }
    //)
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
            //               }
            //           }
        }


    }

    fun addTrackInHistory(track:Track) {
        searchHistoryInteractor.addTrackInHistory(track)
    }

        companion object {
            private const val SEARCH_DEBOUNCE_DELAY = 2000L
        }
    }
