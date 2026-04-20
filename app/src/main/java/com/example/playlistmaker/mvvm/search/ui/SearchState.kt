package com.example.playlistmaker.mvvm.search.ui

import com.example.playlistmaker.mvvm.search.domain.model.Track

data class SearchState(val searchStatus:SearchStatus, val searchResult:List<Track>)

