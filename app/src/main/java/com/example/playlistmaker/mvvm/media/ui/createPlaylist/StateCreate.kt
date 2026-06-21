package com.example.playlistmaker.mvvm.media.ui.createPlaylist

import android.net.Uri

data class StateCreate(
    val uri: Uri?,
    val title: String,
    val description: String
)
