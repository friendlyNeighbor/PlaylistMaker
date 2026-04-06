package com.example.playlistmaker.mvvm.search.domain

import com.example.playlistmaker.domain.models.Track

data class SearchState(val searchStatus:SearchStatus, val searchResult:List<Track>) {
}

