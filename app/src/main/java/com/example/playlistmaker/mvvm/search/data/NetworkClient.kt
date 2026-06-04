package com.example.playlistmaker.mvvm.search.data

import com.example.playlistmaker.mvvm.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}