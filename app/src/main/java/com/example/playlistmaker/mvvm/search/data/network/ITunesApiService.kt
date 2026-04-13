package com.example.playlistmaker.mvvm.search.data.network

import com.example.playlistmaker.mvvm.search.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("search?entity=song")
    fun searchSong(@Query("term") text: String): Call<TrackSearchResponse>
}