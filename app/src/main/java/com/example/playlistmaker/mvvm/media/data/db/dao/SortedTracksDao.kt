package com.example.playlistmaker.mvvm.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mvvm.media.data.db.entity.SortedTrackEntity

@Dao
interface SortedTracksDao {
    @Insert(entity = SortedTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: SortedTrackEntity)

    @Query("DELETE FROM sorted_table WHERE trackId = :id")
    suspend fun deleteTrackById(id: Long)

    @Query("SELECT * FROM sorted_table ORDER BY number DESC")
    suspend fun getSortedList(): List<SortedTrackEntity>

    @Query("SELECT trackId FROM sorted_table")
    suspend fun getIdListOfSorted(): List<Long>
}