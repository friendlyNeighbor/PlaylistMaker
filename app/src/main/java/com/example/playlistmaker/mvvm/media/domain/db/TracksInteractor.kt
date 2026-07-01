package com.example.playlistmaker.mvvm.media.domain.db

import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun getTrackList(): Flow<List<Track>>

    suspend fun addTrack(track: Track)

    suspend fun deleteTrackById(id: Long)

    fun getIdList(): Flow<List<Long>>
}