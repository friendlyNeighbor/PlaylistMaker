package com.example.playlistmaker.mvvm.search.ui

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.player.ui.AudioPlayerActivity
import com.example.playlistmaker.mvvm.search.domain.api.SearchHistoryInteractor


class SearchViewModel (
    val primaryState: SearchState,
    val trackSearchInteractor: TrackSearchInteractor,
    val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel() {

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

    fun goToPlayer(track:Track, context: Context) {
        searchHistoryInteractor.addTrackInHistory(track)
        val intent = Intent(context, AudioPlayerActivity::class.java)
        intent.putExtra(TRACK, track)
        context.startActivity(intent)
    }

        companion object {
            private const val SEARCH_DEBOUNCE_DELAY = 2000L
            private const val TRACK = "TRACK"
        }
    }
