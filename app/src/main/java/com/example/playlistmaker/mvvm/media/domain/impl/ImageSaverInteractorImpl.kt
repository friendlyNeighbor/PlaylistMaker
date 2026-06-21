package com.example.playlistmaker.mvvm.media.domain.impl

import android.net.Uri
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverRepository

class ImageSaverInteractorImpl(private val imageSaverRepository: ImageSaverRepository): ImageSaverInteractor {
    override fun saveImage(uri: Uri, name: String) {
        imageSaverRepository.saveImage(uri, name)
    }
    override fun getImage(name: String):Uri? {
        return imageSaverRepository.getImage(name)
    }

    override fun deleteImage(name: String) {
        return imageSaverRepository.deleteImage(name)
    }
}