package com.example.playlistmaker.mvvm.media.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class ImageSaverRepositoryImpl(private val context: Context) : ImageSaverRepository {

    override suspend fun saveImage(uri: Uri, id: Long): Boolean = withContext(Dispatchers.IO) {
        val name = id.toString()
        val albumDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?.let { File(it, ALBUM_NAME) }
            ?: return@withContext false

        if (!albumDir.exists()) {
            albumDir.mkdirs()
        }

        val file = File(albumDir, "$name.jpg")

        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        return@withContext try {
            inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext false
            outputStream = file.outputStream()

            val bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                return@withContext false
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    override fun getImage(id: Long): Uri? {
        val name = id.toString()
        val albumDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?.let { File(it, ALBUM_NAME) }
            ?: return null

        val file = File(albumDir, "$name.jpg")
        val uri = if (file.exists()) file.toUri() else null
        return uri
    }

    override fun deleteImage(id: Long) {
        val name = id.toString()
        val albumDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?.let { File(it, ALBUM_NAME) }
            ?: return

        val file = File(albumDir, "$name.jpg")
        file.delete()
    }

    companion object {
        private const val ALBUM_NAME = "myalbum"
    }
}