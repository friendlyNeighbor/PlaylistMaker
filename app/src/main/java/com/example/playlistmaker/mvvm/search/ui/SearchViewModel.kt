package com.example.playlistmaker.mvvm.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.creator.Creator

class SearchViewModel (primaryState: SearchState) : ViewModel() {

    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
    private val trackSearchInteractor = Creator.provideTrackSearchInteractor()

    private val searchLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SearchState> = searchLiveData

    private var textInFocus = false
    private var text = ""

    private val handler = Handler(Looper.getMainLooper())

    fun editTextInFocus() {
        textInFocus = true
        textWasChanged("")
    }

    fun textWasChanged(incomingText:String) {
        text = incomingText.trimStart()

        if(textInFocus) {
            val trackListHistory=searchHistoryInteractor.getTrackListHistory()
            if (text.isEmpty()) {
                handler.removeCallbacks(searchRunnable)
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
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private val searchRunnable = Runnable { searchTrack() }

    private fun searchTrack() {
        val trackList: MutableList<Track> = mutableListOf()
        trackSearchInteractor.searchTrack(
            text,
            object : TrackSearchInteractor.TrackConsumer {
                override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                    handler.post {
                        if (errorMessage != null || foundTrack == null) {
                            searchLiveData.postValue(SearchState(SearchStatus.CONNECTION_PROBLEM, trackList))
                        } else {
                            trackList.clear()
                            trackList.addAll(foundTrack)
                            if (trackList.isEmpty()) {
                                searchLiveData.postValue(SearchState(SearchStatus.NOT_FOUND, trackList))
                            } else
                                searchLiveData.postValue(SearchState(SearchStatus.SEARCH_SUCCESSFUL, trackList))
                        }
                    }
                }
            })
    }

        companion object {
            fun getFactory(value: SearchState): ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    SearchViewModel(value)
                }
            }

            private const val SEARCH_DEBOUNCE_DELAY = 2000L
        }
    }
