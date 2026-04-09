package com.example.playlistmaker.mvvm.search.domain.impl

import com.example.playlistmaker.mvvm.Resource
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TrackSearchRepository) : TrackSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackSearchInteractor.TrackConsumer) {
        executor.execute {
            when(val resource = repository.searchTrack(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
}}