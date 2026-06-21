package com.example.playlistmaker.mvvm.media.data.db.converters


import com.example.playlistmaker.mvvm.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mvvm.media.domain.model.Playlist


class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.title,
            playlist.description,
            null
            )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.title,
            playlist.description,
            null
      )
    }
}