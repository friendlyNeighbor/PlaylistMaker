package com.example.playlistmaker.mvvm.media.data.db


import com.example.playlistmaker.mvvm.media.data.db.converters.TrackInPlaylistsDbConvertor
import com.example.playlistmaker.mvvm.media.data.db.entity.TrackInPlaylistsEntity
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TrackInPlaylistsRepositoryImpl (
    private val trackInPlaylistsDbConvertor: TrackInPlaylistsDbConvertor,
    private val appDatabase: AppDatabase
): FavoritesRepository {
    override fun getFavoritesTrackList(): Flow<List<Track>> = flow{
        val favoritesList = appDatabase.getTrackInPlaylistsDao().getFavoritesList()
        emit(convertFromTrackEntityList(favoritesList))
    }

    private fun convertFromTrackEntityList(favoritesList: List<TrackInPlaylistsEntity>): List<Track> {
        return favoritesList.map { track -> trackInPlaylistsDbConvertor.map(track) }
    }

    override fun addTrackInFavorites(track: Track) {
        GlobalScope.launch {   appDatabase.getTrackInPlaylistsDao().insertTrack(convertToTrackEntity(track)) }
    }

    override fun deleteTrackFromFavoritesById(id: Long) {
        GlobalScope.launch {   appDatabase.getTrackInPlaylistsDao().deleteTrackById(id) }
    }

    override fun getFavoritesIdList(): Flow<List<Long>> = flow {
        val idList = appDatabase.getTrackInPlaylistsDao().getIdListOfFavorites()
        emit(idList)
    }

    private fun convertToTrackEntity(track: Track): TrackInPlaylistsEntity {
        return trackInPlaylistsDbConvertor.map(track) }

}