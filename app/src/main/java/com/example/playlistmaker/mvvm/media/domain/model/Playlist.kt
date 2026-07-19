package com.example.playlistmaker.mvvm.media.domain.model

import android.net.Uri

data class Playlist(
    var id: Long=0,
    var title: String,
    var description: String,
    var idListTracks: List<Long>,
    var uriImage: Uri?
)
