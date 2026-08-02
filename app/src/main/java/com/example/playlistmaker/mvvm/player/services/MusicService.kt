package com.example.playlistmaker.mvvm.player.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.playlistmaker.mvvm.player.services.api.AudioPlayerControl
import com.example.playlistmaker.mvvm.player.ui.PlayingStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.player.ui.PlayerFragment
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.getKoin
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class MusicService : Service(), AudioPlayerControl {

    private val playingStatus = MutableStateFlow<PlayingStatus>(PlayingStatus.Default())
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicServiceBinder()
    private var songUrl = ""
    private var timerJob: Job? = null
    private lateinit var track: Track
    private lateinit var artistName: String
    private lateinit var trackName: String
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var notificationGranted = true

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        createNotificationChannel()
    }

    override fun onDestroy() {
        releasePlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        val trackSaverInteractor = getKoin().get<TrackSaverInteractor>()
        track = trackSaverInteractor.getTrackFromMemory()
        artistName = track.artistName
        trackName = track.trackName
        songUrl = intent?.getStringExtra(SONG_URL) ?: ""
        initMediaPlayer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            checkNotificationPermission()
        if (notificationGranted && playingStatus.value is PlayingStatus.Playing) {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                getForegroundServiceTypeConstant()
            )
        }
    }

    override fun hideNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            checkNotificationPermission()
        if (notificationGranted) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return
        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            playingStatus.value = PlayingStatus.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            timerJob?.cancel()
            playingStatus.value = PlayingStatus.Complitted()
            hideNotification()
        }
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        runTimer()
    }

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val player = mediaPlayer ?: break
                if (!player.isPlaying) {
                    break
                }
                delay(TIMER_UPDATE_DELAY.milliseconds)
                val position = try {
                    player.currentPosition
                } catch (_: IllegalStateException) {
                    break
                }
                playingStatus.value = PlayingStatus.Playing(dateFormat.format(position))
            }
        }
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        playingStatus.value = PlayingStatus.Paused(dateFormat.format(mediaPlayer?.currentPosition))
    }

    override fun getPlayingStatus(): StateFlow<PlayingStatus> = playingStatus

    override fun releasePlayer() {
        timerJob?.cancel()
        timerJob = null
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            player.stop()
        }
        player.setOnPreparedListener(null)
        player.setOnCompletionListener(null)

        try {
            player.release()
        } catch (e: IllegalStateException) {
            Log.w("mylog", "Tried to release already released MediaPlayer", e)
        } finally {
            mediaPlayer = null
        }
        playingStatus.value = PlayingStatus.Default()
        hideNotification()
    }


    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText("$artistName - $trackName")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        notificationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    private companion object {
        const val NOTIFICATION_TITLE = "Playlist Maker"
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Music service"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Service for playing music"
        const val SERVICE_NOTIFICATION_ID = 100
        private const val TIMER_UPDATE_DELAY = 300L
        const val SONG_URL = PlayerFragment.SONG_URL
    }
}
