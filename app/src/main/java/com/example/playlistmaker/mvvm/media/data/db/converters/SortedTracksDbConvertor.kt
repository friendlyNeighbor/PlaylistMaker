package com.example.playlistmaker.mvvm.media.data.db.converters

import com.example.playlistmaker.mvvm.media.data.db.entity.SortedTrackEntity
import com.example.playlistmaker.mvvm.search.domain.model.Track

class SortedTracksDbConvertor {
    fun map(track: Track): SortedTrackEntity {
        return SortedTrackEntity(
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

    fun map(track: SortedTrackEntity): Track {
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