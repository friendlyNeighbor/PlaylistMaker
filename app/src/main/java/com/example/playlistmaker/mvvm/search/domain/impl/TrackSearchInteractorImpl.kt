package com.example.playlistmaker.mvvm.search.domain.impl

import com.example.playlistmaker.mvvm.Resource
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
//import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TrackSearchRepository) : TrackSearchInteractor {

  //  private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
        /*        executor.execute {
            when(val resource = repository.searchTrack(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
     }
 */
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