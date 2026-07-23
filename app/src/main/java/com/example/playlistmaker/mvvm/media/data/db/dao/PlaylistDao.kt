package com.example.playlistmaker.mvvm.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.mvvm.media.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("DELETE FROM playlists_table WHERE title = :title")
    suspend fun deletePlaylistByName(title: String)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylistList(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists_table WHERE title = :title")
    suspend fun getPlaylistByTitle(title: String): PlaylistEntity

    @Query("DELETE FROM playlists_table WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

}