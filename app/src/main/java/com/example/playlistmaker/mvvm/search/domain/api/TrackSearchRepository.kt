package com.example.playlistmaker.mvvm.search.domain.api

import com.example.playlistmaker.mvvm.Resource
import com.example.playlistmaker.mvvm.search.domain.model.Track

interface TrackSearchRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}