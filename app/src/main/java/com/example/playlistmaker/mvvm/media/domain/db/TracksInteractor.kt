package com.example.playlistmaker.mvvm.media.domain.db

import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun getTrackList(): Flow<List<Track>>

    fun addTrack(track: Track)

    fun deleteTrackById(id: Long)

    fun getIdList(): Flow<List<Long>>
}