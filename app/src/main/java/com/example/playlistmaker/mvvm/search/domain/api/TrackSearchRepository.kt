package com.example.playlistmaker.mvvm.search.domain.api

import com.example.playlistmaker.mvvm.Resource
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchRepository {
   fun searchTrack(expression: String): Flow<Resource<List<Track>>>
}