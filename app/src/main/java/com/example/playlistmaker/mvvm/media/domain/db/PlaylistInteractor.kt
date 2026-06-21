package com.example.playlistmaker.mvvm.media.domain.db

import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getListOfPlaylists(): Flow<List<Playlist>>

    fun addNewPlaylist(playlist: Playlist)

    fun deletePlaylist(title: String)
}