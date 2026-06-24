package com.example.playlistmaker.mvvm.media.data.db.converters

import com.example.playlistmaker.mvvm.media.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.mvvm.search.domain.model.Track


class FavoritesTracksDbConvertor {
    fun map(track: Track): FavoritesTrackEntity {
        return FavoritesTrackEntity(
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

    fun map(track: FavoritesTrackEntity): Track {
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
