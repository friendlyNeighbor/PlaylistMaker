package com.example.playlistmaker.mvvm.player.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    // Переменная для хранения MediaPlayer
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicServiceBinder()
    // Глобальная переменная для хранения ссылки на песню
    private var songUrl = ""

    // Методы класса Service

    override fun onCreate() {
        super.onCreate()
        Log.d("myerr", "   onCreate Service()")
        mediaPlayer = MediaPlayer()

    }

    override fun onDestroy() {
        releasePlayer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        initMediaPlayer()
        Log.d("myerr", "songUrl = $songUrl")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }
    /*
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        initMediaPlayer()
        return Service.START_NOT_STICKY
    }

     */

    // Методы управления Media Player

    // Первичная инициализация плеера
    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            Log.d(LOG_TAG, "Media Player prepared")
        }
        mediaPlayer?.setOnCompletionListener {
            Log.d(LOG_TAG, "Playback completed")
        }
    }

    // Запуск воспроизведения
    fun startPlayer() {
        mediaPlayer?.start()
    }

    // Приостановка воспроизведения
    fun pausePlayer() {
        mediaPlayer?.pause()
    }

    // Освобождаем все ресурсы, выделенные для плеера
    private fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    private companion object {
        const val LOG_TAG = "MusicService"
    }
}