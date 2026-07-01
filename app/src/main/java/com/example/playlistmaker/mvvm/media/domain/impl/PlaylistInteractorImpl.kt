package com.example.playlistmaker.mvvm.media.domain.impl

import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistRepository
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override fun getListOfPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getListOfPlaylists()
    }

    override suspend fun addNewPlaylist(playlist: Playlist) {
        playlistRepository.addNewPlaylist(playlist)
    }

    override suspend fun deletePlaylist(title: String) {
        playlistRepository.deletePlaylist(title)
    }

    override fun getPlaylist(title: String): Flow<Playlist> {
        return playlistRepository.getPlaylist(title)
    }
}