package com.example.playlistmaker.mvvm.media.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverRepository
import java.io.File
import java.io.FileOutputStream

class ImageSaverRepositoryImpl(private val context: Context): ImageSaverRepository {
    override fun saveImage(uri: Uri, name: String) {
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME)
            if (!filePath.exists()){
                filePath.mkdirs()
            }
            val file = File(filePath, "$name.jpg")
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
    override fun getImage(name: String):Uri? {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME)
        val file = File(filePath, "$name.jpg")
        return if (file.exists()) file.toUri() else null
    }

    override fun deleteImage(name: String) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME)
        val file = File(filePath, "$name.jpg")
        file.delete()
            }

    companion object {
        private const val ALBUM_NAME = "myalbum"
    }
}