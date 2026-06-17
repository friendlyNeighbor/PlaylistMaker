package com.example.playlistmaker.mvvm.media.ui.createPlaylist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePlaylistViewModel: ViewModel() {
    private val createPlaylistLiveData = MutableLiveData<StateCreate>()
    fun getLiveData():LiveData<StateCreate> = createPlaylistLiveData



}