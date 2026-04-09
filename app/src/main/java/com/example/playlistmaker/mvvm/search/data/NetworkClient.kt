package com.example.playlistmaker.mvvm.search.data

import com.example.playlistmaker.mvvm.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}