package com.example.playlistmaker.mvvm.media.ui.playlistScreen

import android.net.Uri
import com.example.playlistmaker.mvvm.search.domain.model.Track

data class PlaylistScreenState (
    val title: String,
    val description: String,
    val quantity: Int,
    val duration: Int,
    val imageUrl: Uri?,
    val trackList: List<Track>,
    var playlistDeleted:Boolean  = false
)