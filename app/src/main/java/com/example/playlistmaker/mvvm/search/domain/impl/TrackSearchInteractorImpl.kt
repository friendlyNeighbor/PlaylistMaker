package com.example.playlistmaker.mvvm.search.domain.impl

import com.example.playlistmaker.mvvm.Resource
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackSearchInteractorImpl(private val repository: TrackSearchRepository) : TrackSearchInteractor {

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {

        return repository.searchTrack(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}