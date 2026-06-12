package com.example.playlistmaker.mvvm.media.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table", indices = [Index(value = ["trackId"], unique = true)])

data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val number:Long,
    val trackId:Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl:String
)

