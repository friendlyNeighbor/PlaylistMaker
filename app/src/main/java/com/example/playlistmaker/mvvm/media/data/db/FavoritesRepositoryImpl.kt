package com.example.playlistmaker.mvvm.media.data.db

import com.example.playlistmaker.mvvm.media.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.mvvm.media.data.db.entity.TrackEntity
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FavoritesRepositoryImpl(
    private val trackDbConvertor: TrackDbConvertor,
    private val appDatabase: AppDatabase
): FavoritesRepository {
    override fun getFavoritesTrackList(): Flow<List<Track>> = flow{
        val favoritesList = appDatabase.getTrackDao().getFavoritesList()
        emit(convertFromTrackEntityList(favoritesList))
    }

    private fun convertFromTrackEntityList(favoritesList: List<TrackEntity>): List<Track> {
        return favoritesList.map { track -> trackDbConvertor.map(track) }
    }

    override fun addTrackInFavorites(track: Track) {
        GlobalScope.launch {   appDatabase.getTrackDao().insertTrack(convertToTrackEntity(track)) }
    }

    override fun deleteTrackFromFavoritesById(id: Long) {
        GlobalScope.launch {   appDatabase.getTrackDao().deleteTrackById(id) }
    }

    override fun getFavoritesIdList(): Flow<List<Long>> = flow {
        val idList = appDatabase.getTrackDao().getIdListOfFavorites()
        emit(idList)
    }

    private fun convertFromTrackEntity(track: TrackEntity): Track {
        return trackDbConvertor.map(track)
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track) }

}