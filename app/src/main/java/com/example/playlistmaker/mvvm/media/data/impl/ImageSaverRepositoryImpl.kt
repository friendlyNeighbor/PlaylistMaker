package com.example.playlistmaker.mvvm.media.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverRepository
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class ImageSaverRepositoryImpl(private val context: Context) : ImageSaverRepository {

    override suspend fun saveImage(uri: Uri, id: Long): Boolean {
        Log.d("MyError", "saveImage(uri=$uri, id=$id)")
        val name = id.toString()
        val albumDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?.let { File(it, ALBUM_NAME) }
            ?: return false

        if (!albumDir.exists()) {
            albumDir.mkdirs()
        }

        val file = File(albumDir, "$name.jpg")

        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        return try {
            inputStream = context.contentResolver.openInputStream(uri) ?: return false
            outputStream = file.outputStream()

            val bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                Log.d("MyError", " Не удалось декодировать картинку (слишком большая, битый файл и т.п.)")
                return false
            }

            // Сжимаем и пишем
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            Log.d("MyError", " возвращаем true как успешное сохранение картинки")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("MyError", " возвращаем  false, картинка не сохранилась")
            false
        } finally {
            // Обязательно закрываем потоки
            inputStream?.close()
            outputStream?.close()
        }
    }

    override fun getImage(id: Long): Uri? {
        Log.d("MyError", "getImage(id=$id)")
        val name = id.toString()
        val albumDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?.let { File(it, ALBUM_NAME) }
            ?: return null

        val file = File(albumDir, "$name.jpg")
        val uri = if (file.exists()) file.toUri() else null
        Log.d("MyError", "getImage(uri=$uri, id=$id)")
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