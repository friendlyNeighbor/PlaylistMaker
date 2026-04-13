package com.example.playlistmaker.mvvm.search.data.dto

data class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>): Response()