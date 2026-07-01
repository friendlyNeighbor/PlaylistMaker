package com.example.playlistmaker.mvvm.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mvvm.media.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists_table WHERE title = :title")
    suspend fun deletePlaylistByName(title: String)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylistList(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists_table WHERE title = :title")
    suspend fun getPlaylistByTitle(title: String): PlaylistEntity

}