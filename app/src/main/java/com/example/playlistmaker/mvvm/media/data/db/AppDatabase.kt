package com.example.playlistmaker.mvvm.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mvvm.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.mvvm.media.data.db.dao.FavoritesTrackDao
import com.example.playlistmaker.mvvm.media.data.db.dao.SortedTracksDao
import com.example.playlistmaker.mvvm.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mvvm.media.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.mvvm.media.data.db.entity.SortedTrackEntity

@Database(version = 3, entities = [FavoritesTrackEntity::class, PlaylistEntity::class, SortedTrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun getFavoritesTrackDao(): FavoritesTrackDao

    abstract fun getPlaylistDao(): PlaylistDao

    abstract fun getSortedTrackDao(): SortedTracksDao
}