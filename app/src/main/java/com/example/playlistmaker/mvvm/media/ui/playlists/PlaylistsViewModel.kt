package com.example.playlistmaker.mvvm.media.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel: ViewModel() {
    private val playlistsLiveData = MutableLiveData<PlaylistsState>()
    fun getLiveData(): LiveData<PlaylistsState> = playlistsLiveData

    fun playlistsIsEmpty() {
        playlistsLiveData.postValue(PlaylistsState.EMPTY)
    }
}