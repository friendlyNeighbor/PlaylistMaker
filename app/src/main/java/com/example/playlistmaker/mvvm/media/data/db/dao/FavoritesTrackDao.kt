package com.example.playlistmaker.mvvm.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.mvvm.media.data.db.entity.FavoritesTrackEntity
import androidx.room.Query

@Dao
interface FavoritesTrackDao {
    @Insert(entity = FavoritesTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: FavoritesTrackEntity)

    @Query("DELETE FROM favorites_table WHERE trackId = :id")
    suspend fun deleteTrackById(id: Long)

    @Query("SELECT * FROM favorites_table ORDER BY number DESC")
    suspend fun getFavoritesList(): List<FavoritesTrackEntity>

    @Query("SELECT trackId FROM favorites_table")
    suspend fun getIdListOfFavorites(): List<Long>
}
