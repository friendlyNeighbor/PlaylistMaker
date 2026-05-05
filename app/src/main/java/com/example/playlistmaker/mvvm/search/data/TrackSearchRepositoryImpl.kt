package com.example.playlistmaker.mvvm.search.data

import com.example.playlistmaker.mvvm.Resource
import com.example.playlistmaker.mvvm.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.mvvm.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.search.domain.api.TrackSearchRepository
import java.text.SimpleDateFormat
import java.util.Locale

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient): TrackSearchRepository {
        override fun searchTrack(expression: String): Resource<List<Track>> {
            val response = networkClient.doRequest(TrackSearchRequest(expression))

            return when (response.resultCode) {
                -1 -> {
                    Resource.Error("Проверьте подключение к интернету")
                }
                200 -> {
                    Resource.Success((response as TrackSearchResponse).results.map {
                        Track(
                            it.trackName?:"",
                            it.artistName?:"",
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(it.trackTimeMillis?:0),
                            it.artworkUrl100?:"",
                            it.trackId?:0,
                            it.collectionName?:"",
                            it.releaseDate?:"",
                            it.primaryGenreName?:"",
                            it.country?:"",
                            it.previewUrl?:""
                        )
                    })
                }
                else -> {
                    Resource.Error("Ошибка сервера")
                }
            }
        }
    }