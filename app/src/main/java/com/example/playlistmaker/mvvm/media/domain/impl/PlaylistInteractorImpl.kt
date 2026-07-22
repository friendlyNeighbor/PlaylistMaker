package com.example.playlistmaker.mvvm.media.domain.impl

import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistRepository
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override fun getListOfPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getListOfPlaylists()
    }

    override suspend fun addNewPlaylist(playlist: Playlist): Long {
       return playlistRepository.addNewPlaylist(playlist)
    }

    override suspend fun deletePlaylistByTitle(title: String) {
        playlistRepository.deletePlaylistByTitle(title)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override fun getPlaylistByTitle(title: String): Flow<Playlist> {
        return playlistRepository.getPlaylistByTitle(title)
    }

    override fun getPlaylistById(id: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(id)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun deleteTrackFromPlaylistById(idTrack: Long, idPlaylist: Long): Boolean {
        return playlistRepository.deleteTrackFromPlaylistById(idTrack, idPlaylist)
    }

    override suspend fun deletePlaylistAndReturnListTrackId(playlist: Playlist): List<Long> {
        return playlistRepository.deletePlaylistAndReturnListTrackId(playlist)
    }

}