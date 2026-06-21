package com.example.playlistmaker.mvvm.media.ui.playlists


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor, private val imageSaverInteractor: ImageSaverInteractor): ViewModel() {
    private val playlistsLiveData = MutableLiveData<PlaylistsState>()
    fun getLiveData(): LiveData<PlaylistsState> = playlistsLiveData

    fun  readPlaylistDb() {
        viewModelScope.launch {
            val listOfPlaylist = playlistInteractor.getListOfPlaylists().first()
            if (listOfPlaylist.isEmpty())
                playlistsLiveData.postValue(PlaylistsState(emptyList()))
            else {
                for (playlist in listOfPlaylist) {
                    playlist.uriImage=imageSaverInteractor.getImage(playlist.title)
                }
                playlistsLiveData.postValue(PlaylistsState(listOfPlaylist))
            }
        }

    }
}