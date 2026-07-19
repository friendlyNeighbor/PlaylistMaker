package com.example.playlistmaker.mvvm.media.ui.createPlaylist

import android.net.Uri

data class StateCreate(
    val title: String?,
    val description: String?,
    val uri: Uri?,
    val savingComplete: Boolean
)
