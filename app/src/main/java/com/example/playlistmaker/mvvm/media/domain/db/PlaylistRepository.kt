package com.example.playlistmaker.mvvm.media.domain.db

import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {
    fun getListOfPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(title: String)

    fun getPlaylist(title:String): Flow<Playlist>
}