package com.example.playlistmaker.mvvm.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.TracksInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.player.services.api.AudioPlayerControl
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val favoritesTracksInteractor: TracksInteractor,
    private val trackSaverInteractor: TrackSaverInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val imageSaverInteractor: ImageSaverInteractor,
    private val sortedTracksInteractor: TracksInteractor,
) :
    ViewModel() {

    private val playerLiveData = MutableLiveData<PlayerState>()
    fun getLiveData(): LiveData<PlayerState> = playerLiveData

    private val playingTrack: Track = getTrack()
    private var playingStatus: PlayingStatus = PlayingStatus.Default()
    private var isFavoriteTrack = false
    private var listOfPlaylist: List<Playlist> = emptyList()
    private var isInPlaylistYet: Boolean? = null
    private var audioPlayerControl: AudioPlayerControl? = null

    init {
        checkOnFavorite()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayingStatus().collect {
                playingStatus = it
                postLiveData()
            }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun showNotification() {
        audioPlayerControl?.showNotification()
    }

    fun hideNotification() {
        audioPlayerControl?.hideNotification()
    }

    fun playbackControl() {
        when (playingStatus) {
            is PlayingStatus.Prepared ->  startPlayer()
            is PlayingStatus.Paused ->  startPlayer()
            is PlayingStatus.Complitted ->  startPlayer()
            is PlayingStatus.Playing ->  pausePlayer()
            is PlayingStatus.Default ->  pausePlayer()
        }
    }

    private fun startPlayer() {
        audioPlayerControl?.startPlayer()
    }

    private fun pausePlayer() {
        audioPlayerControl?.pausePlayer()
    }

    private fun checkOnFavorite() {
        viewModelScope.launch {
            val list = favoritesTracksInteractor.getIdList().first()
            isFavoriteTrack = list.contains(playingTrack.trackId)
            postLiveData()
        }
    }

    fun changeLike() {
        isFavoriteTrack = !isFavoriteTrack
        postLiveData()
    }

    fun refreshDataBase() {
        viewModelScope.launch {
        if (isFavoriteTrack)
             favoritesTracksInteractor.addTrack(playingTrack)
        else
             favoritesTracksInteractor.deleteTrackById(playingTrack.trackId)
        }
    }

    fun readPlaylistDb() {
        viewModelScope.launch {
            val list = playlistInteractor.getListOfPlaylists().first()
            if (list.isNotEmpty()) {
                    for (playlist in list) {
                        playlist.uriImage = imageSaverInteractor.getImage(playlist.id)
                    }
                listOfPlaylist = list
                postLiveData()
            }
        }
    }

    private fun postLiveData() {
        playerLiveData.postValue(
            PlayerState(
                playingStatus,
                playingTrack,
                isFavoriteTrack,
                listOfPlaylist,
                isInPlaylistYet
            )
        )
    }

    fun getTrack(): Track {
        return trackSaverInteractor.getTrackFromMemory()
    }

    fun addTrackInSorted() {
        viewModelScope.launch { sortedTracksInteractor.addTrack(playingTrack) }
    }

    fun addTrackIdInPlaylist(playlist:Playlist) {
            if (playlist.idListTracks.contains(playingTrack.trackId)) {
                isInPlaylistYet = true
                postLiveData()
            } else {
                playlist.idListTracks += listOf(playingTrack.trackId)
                viewModelScope.launch { playlistInteractor.addNewPlaylist(playlist)}
                isInPlaylistYet = false
                postLiveData()
            }
    }

    fun resetIsInPlaylist() {
        isInPlaylistYet = null
        postLiveData()
    }

}
