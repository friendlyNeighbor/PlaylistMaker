package com.example.playlistmaker.mvvm.media.ui.playlistScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.db.TracksInteractor
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ViewModelPlaylistScreen(private val playlistInteractor: PlaylistInteractor,
                              private val imageSaverInteractor: ImageSaverInteractor,
                              private val sortedTracksInteractor: TracksInteractor,
                              private val trackSaverInteractor: TrackSaverInteractor,
                              private val sharingInteractor: SharingInteractor): ViewModel() {
    private val playlistLiveData = MutableLiveData<PlaylistScreenState>()
    fun getLiveData(): LiveData<PlaylistScreenState> = playlistLiveData

    private lateinit var playlist:Playlist
    private lateinit var allTracksInAllPlaylists: List<Track>

    fun loadPlaylistById(id: Long) {
        viewModelScope.launch {
            playlist = playlistInteractor.getPlaylistById(id).first()
            val id = playlist.id
            allTracksInAllPlaylists = sortedTracksInteractor.getTrackList().first()
            val duration = (calculateDuration()/60000).toInt()
            val imageUri = imageSaverInteractor.getImage(id)
            val trackList = createTrackList()
            playlistLiveData.postValue(PlaylistScreenState(playlist.title, playlist.description, playlist.idListTracks.size, duration, imageUri, trackList))
        }
    }

    private fun calculateDuration(): Long {
        var duration = 0L
        for (track in allTracksInAllPlaylists) {
            if(playlist.idListTracks.contains(track.trackId)) {
                duration+=track.trackTime.toMillis()
            }
        }
        return duration
    }

    private fun String.toMillis(): Long {
        val parts = this.split(":")
        if (parts.size != 2) throw IllegalArgumentException("Ожидался формат mm:ss")

        val minutes = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Некорректные минуты")
        val seconds = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Некорректные секунды")

        if (minutes < 0 || seconds < 0 || seconds > 59) {
            throw IllegalArgumentException("Недопустимые значения времени")
        }

        return (minutes * 60L + seconds) * 1000L
    }

    private fun createTrackList(): List<Track> {
        val trackList=mutableListOf<Track>()
        for (track in allTracksInAllPlaylists) {
            if(playlist.idListTracks.contains(track.trackId)) {
                trackList.add(track)
            }
        }
        return trackList
    }

    fun addTrackInMemory(track: Track) {
        trackSaverInteractor.addTrackInMemory(track)
    }

    fun deleteTrack(track:Track) {
        val newIdListTracks = playlist.idListTracks.filterNot { it == track.trackId }
        val updatePlaylist = playlist.copy(idListTracks = newIdListTracks)
        viewModelScope.launch {
            playlistInteractor.addNewPlaylist(updatePlaylist)
            loadPlaylistById(playlist.id)

            var trackNotFound = true
            val playlistList = playlistInteractor.getListOfPlaylists().first()
            for (playlist in playlistList) {
                if(playlist.idListTracks.contains(track.trackId))
                    trackNotFound = false
            }
            if (trackNotFound)
                sortedTracksInteractor.deleteTrackById(track.trackId)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
            playlistLiveData.postValue(PlaylistScreenState("", "", 0,0,null,emptyList(), true ))
        }
    }

    fun sharePlaylist(quantity: String) {
        var message = "${playlist.title} \n${playlist.description}\n${quantity}"
        var n=0
        for (track in allTracksInAllPlaylists) {
            if(playlist.idListTracks.contains(track.trackId)) {
                n++
                val string = "\n$n. ${track.artistName} - ${track.trackName} (${track.trackTime})"
                message += string
            }
        }
        sharingInteractor.sharePlaylist(message)
    }

}
