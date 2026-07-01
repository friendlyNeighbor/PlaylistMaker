package com.example.playlistmaker.mvvm.media.data.db

import com.example.playlistmaker.mvvm.media.data.db.converters.PlaylistDbConvertor
import com.example.playlistmaker.mvvm.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistRepository
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PlaylistRepositoryImpl(
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val appDatabase: AppDatabase
) : PlaylistRepository {

    override fun getListOfPlaylists(): Flow<List<Playlist>> = flow {
        val listOfPlaylist = appDatabase.getPlaylistDao().getPlaylistList()
        emit(convertFromPlaylistEntityList(listOfPlaylist))
    }

    override suspend fun addNewPlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().insertPlaylist(convertToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(title: String) {
        appDatabase.getPlaylistDao().deletePlaylistByName(title)
    }

    override fun getPlaylist(title:String):Flow<Playlist> = flow {
        val playlist = appDatabase.getPlaylistDao().getPlaylistByTitle(title)
        emit(convertFromPlaylistEntity(playlist))
    }

    private fun convertFromPlaylistEntityList(listOfPlaylist: List<PlaylistEntity>): List<Playlist> {
        return listOfPlaylist.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertFromPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistDbConvertor.map(playlist)
    }

    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }

}