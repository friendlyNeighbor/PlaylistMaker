package com.example.playlistmaker.mvvm.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mvvm.media.data.db.entity.TrackInPlaylistsEntity

@Dao
interface TrackInPlaylistsDao {
    @Insert(entity = TrackInPlaylistsEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistsEntity)

    @Query("DELETE FROM sorted_table WHERE trackId = :id")
    suspend fun deleteTrackById(id: Long)

    @Query("SELECT * FROM sorted_table ORDER BY number DESC")
    suspend fun getFavoritesList(): List<TrackInPlaylistsEntity>

    @Query("SELECT trackId FROM sorted_table")
    suspend fun getIdListOfFavorites(): List<Long>
}