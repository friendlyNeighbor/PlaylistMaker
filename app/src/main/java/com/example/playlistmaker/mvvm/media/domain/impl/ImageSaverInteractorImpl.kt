package com.example.playlistmaker.mvvm.media.domain.impl

import android.net.Uri
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverRepository

class ImageSaverInteractorImpl(private val imageSaverRepository: ImageSaverRepository): ImageSaverInteractor {
    override suspend fun saveImage(uri: Uri, id: Long): Boolean {
        return imageSaverRepository.saveImage(uri, id)
    }
    override fun getImage(id: Long):Uri? {
        return imageSaverRepository.getImage(id)
    }

    override fun deleteImage(id: Long) {
        return imageSaverRepository.deleteImage(id)
    }
}