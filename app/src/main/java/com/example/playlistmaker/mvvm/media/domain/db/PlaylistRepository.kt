package com.example.playlistmaker.mvvm.media.domain.db


import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {
    fun getListOfPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(playlist: Playlist): Long

    suspend fun deletePlaylistByTitle(title: String)
    suspend fun deletePlaylistById(id: Long)
    suspend fun deletePlaylist(playlist: Playlist)


    fun getPlaylistByTitle(title:String): Flow<Playlist>
    fun getPlaylistById(id: Long): Flow<Playlist>

    suspend fun updatePlaylist(playlist: Playlist)
}