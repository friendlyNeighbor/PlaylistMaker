package com.example.playlistmaker.mvvm.media.domain.db


import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoritesTrackList(): Flow<List<Track>>

    fun addTrackInFavorites(track: Track)

    fun deleteTrackFromFavoritesById(id: Long)

    fun getFavoritesIdList(): Flow<List<Long>>
}
