package com.example.playlistmaker.mvvm.media.ui.createPlaylist

import android.net.Uri

data class StateCreate(
    val title: String = "",
    val description: String = "",
    val uri: Uri? = null,
    val modeEditing: Boolean = false,
    val dialogVisibility: Boolean = false,
    val savingComplete: Boolean = false,
    val screenMayBeClosed: Boolean = false
)
