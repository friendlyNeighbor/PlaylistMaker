package com.example.playlistmaker.mvvm.media.data.db.converters

import com.example.playlistmaker.mvvm.media.data.db.entity.TrackInPlaylistsEntity
import com.example.playlistmaker.mvvm.search.domain.model.Track

class TrackInPlaylistsDbConvertor {
    fun map(track: Track): TrackInPlaylistsEntity {
        return TrackInPlaylistsEntity(
            track.number,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackInPlaylistsEntity): Track {
        return Track(
            track.number,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}