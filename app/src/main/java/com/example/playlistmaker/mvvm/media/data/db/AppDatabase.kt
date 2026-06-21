package com.example.playlistmaker.mvvm.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mvvm.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.mvvm.media.data.db.dao.TrackDao
import com.example.playlistmaker.mvvm.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mvvm.media.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun getTrackDao(): TrackDao

    abstract fun getPlaylistDao(): PlaylistDao
}