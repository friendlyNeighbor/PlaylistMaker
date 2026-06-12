package com.example.playlistmaker.mvvm.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.mvvm.media.data.db.entity.TrackEntity
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM favorites_table WHERE trackId = :id")
    suspend fun deleteTrackById(id: Long)

    @Query("SELECT * FROM favorites_table ORDER BY number DESC")
    suspend fun getFavoritesList(): List<TrackEntity>

    @Query("SELECT trackId FROM favorites_table")
    suspend fun getIdListOfFavorites(): List<Long>
}
