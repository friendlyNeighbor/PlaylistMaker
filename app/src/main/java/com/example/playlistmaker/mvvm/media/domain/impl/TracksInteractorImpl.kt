package com.example.playlistmaker.mvvm.media.domain.impl

import com.example.playlistmaker.mvvm.media.domain.db.TracksInteractor
import com.example.playlistmaker.mvvm.media.domain.db.TracksRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val tracksRepository: TracksRepository
): TracksInteractor {
    override fun getTrackList(): Flow<List<Track>> {
        return tracksRepository.getTrackList()
    }

    override fun addTrack(track: Track) {
        tracksRepository.addTrack(track)
    }

    override fun deleteTrackById(id: Long) {
        tracksRepository.deleteTrackById(id)
    }

    override fun getIdList(): Flow<List<Long>> {
        return tracksRepository.getIdList()
    }
}