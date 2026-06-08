package com.example.playlistmaker.mvvm.media.domain.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mvvm.media.data.db.entity.TrackEntity
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoritesTrackList(): Flow<List<Track>>

    fun addTrackInFavorites(track: Track)

    fun deleteTrackFromFavorites(track: Track)

    fun getFavoritesIdList(): Flow<List<Long>>
}
