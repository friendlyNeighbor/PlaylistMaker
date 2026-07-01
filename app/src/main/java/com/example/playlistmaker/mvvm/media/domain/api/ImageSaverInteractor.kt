package com.example.playlistmaker.mvvm.media.domain.api

import android.net.Uri

interface ImageSaverInteractor {
    fun saveImage(uri: Uri, name: String)
    fun getImage(name: String):Uri?
    fun deleteImage(name: String)
}