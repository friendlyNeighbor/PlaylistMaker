package com.example.playlistmaker.mvvm.media.data.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table", indices = [Index(value = ["title"], unique = true)])

class PlaylistEntity(
        @PrimaryKey
        var title: String="",
        var description: String="",
        var idListTracks: String="",
        @Ignore
        var uriImage: Uri? = null
    )




