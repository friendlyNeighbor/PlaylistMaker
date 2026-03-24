package com.example.playlistmaker.data

import com.example.playlistmaker.Resource
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.api.TrackRepository
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.String

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
        override fun searchTrack(expression: String): Resource<List<Track>> {
            val response = networkClient.doRequest(TrackSearchRequest(expression))

            return when (response.resultCode) {
                -1 -> {
                    Resource.Error("Проверьте подключение к интернету")
                }
                200 -> {
                    Resource.Success((response as TrackSearchResponse).results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                            it.artworkUrl100,
                            it.trackId,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    })
                }
                else -> {
                    Resource.Error("Ошибка сервера")
                }
            }


/*


            if (response.resultCode == 200) {
                return (response as TrackSearchResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                        it.artworkUrl100,
                        it.trackId,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }
            } else {
                return emptyList()
            }
            */
        }
    }
