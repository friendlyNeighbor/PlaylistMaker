package com.example.playlistmaker.mvvm.media.data.db.converters


import com.example.playlistmaker.mvvm.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import com.google.gson.Gson


class PlaylistDbConvertor {
    private val gson = Gson()
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.title,
            playlist.description,
            gson.toJson(playlist.idListTracks),
            null
            )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        val idListTracks = gson.fromJson(playlist.idListTracks, Array<Long>::class.java).toList()
        return Playlist(
            playlist.title,
            playlist.description,
            idListTracks,
            null
      )
    }
}