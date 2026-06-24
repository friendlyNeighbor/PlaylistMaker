package com.example.playlistmaker.mvvm.media.data.db

import com.example.playlistmaker.mvvm.media.data.db.converters.FavoritesTracksDbConvertor
import com.example.playlistmaker.mvvm.media.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.mvvm.media.domain.db.TracksRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FavoritesTracksDbRepositoryImpl(
    private val favoritesTracksDbConvertor: FavoritesTracksDbConvertor,
    private val appDatabase: AppDatabase
): TracksRepository {
    override fun getTrackList(): Flow<List<Track>> = flow{
        val favoritesList = appDatabase.getFavoritesTrackDao().getFavoritesList()
        emit(convertFromTrackEntityList(favoritesList))
    }

    private fun convertFromTrackEntityList(favoritesList: List<FavoritesTrackEntity>): List<Track> {
        return favoritesList.map { track -> favoritesTracksDbConvertor.map(track) }
    }

    override fun addTrack(track: Track) {
        GlobalScope.launch {   appDatabase.getFavoritesTrackDao().insertTrack(convertToTrackEntity(track)) }
    }

    override fun deleteTrackById(id: Long) {
        GlobalScope.launch {   appDatabase.getFavoritesTrackDao().deleteTrackById(id) }
    }

    override fun getIdList(): Flow<List<Long>> = flow {
        val idList = appDatabase.getFavoritesTrackDao().getIdListOfFavorites()
        emit(idList)
    }

    private fun convertToTrackEntity(track: Track): FavoritesTrackEntity {
        return favoritesTracksDbConvertor.map(track) }

}