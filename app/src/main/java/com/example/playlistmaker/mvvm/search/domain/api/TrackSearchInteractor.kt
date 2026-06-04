package com.example.playlistmaker.mvvm.search.domain.api

import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchInteractor {
    fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>>
}