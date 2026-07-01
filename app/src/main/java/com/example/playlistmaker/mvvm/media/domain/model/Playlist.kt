package com.example.playlistmaker.mvvm.media.domain.model

import android.net.Uri

class Playlist(
    var title: String,
    var description: String,
    var idListTracks: List<Long>,
    var uriImage: Uri?
)
