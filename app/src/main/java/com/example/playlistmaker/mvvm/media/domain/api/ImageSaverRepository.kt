package com.example.playlistmaker.mvvm.media.domain.api

import android.net.Uri

interface ImageSaverRepository {
    suspend fun saveImage(uri: Uri, id: Long): Boolean
    fun getImage(id: Long):Uri?
    fun deleteImage(id: Long)
}