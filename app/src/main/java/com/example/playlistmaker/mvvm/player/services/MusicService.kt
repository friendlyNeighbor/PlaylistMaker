package com.example.playlistmaker.mvvm.player.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
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
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.player.ui.PlayerFragment
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.getKoin
import java.util.Locale

class MusicService : Service(), AudioPlayerControl {

    private var playingStatus = MutableStateFlow<PlayingStatus>(PlayingStatus.Default())
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicServiceBinder()
    private var songUrl = ""
    private var timerJob: Job? = null
    private lateinit var track: Track
    private lateinit var artistName: String
    private lateinit var trackName: String
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        createNotificationChannel()
    }

    override fun onDestroy() {
        releasePlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        songUrl = intent?.getStringExtra(SONG_URL) ?: ""
        initMediaPlayer()
        val trackSaverInteractor = getKoin().get<TrackSaverInteractor>()
        track= trackSaverInteractor.getTrackFromMemory()
        artistName = track.artistName
        trackName = track.trackName
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }



    override fun showNotification() {
        if(playingStatus.value is PlayingStatus.Playing ) {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                getForegroundServiceTypeConstant()
            )
        }
    }

    override fun hideNotification() {
        stopForeground(STOP_FOREGROUND_REMOVE)
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
            playingStatus.value = PlayingStatus.Prepared()
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
            delay(TIMER_UPDATE_DELAY)
            val position = try {
                player.currentPosition
            } catch (e: IllegalStateException) {
                break
            }
            playingStatus.value = PlayingStatus.Playing(dateFormat.format(position))
        }
    }
}

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        playingStatus.value = PlayingStatus.Paused(dateFormat.format(mediaPlayer?.currentPosition))    }

    override fun getPlayingStatus(): StateFlow<PlayingStatus> = playingStatus

    override fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
        timerJob?.cancel()
        playingStatus.value = PlayingStatus.Default()
        hideNotification()
        stopSelf()
    }

private fun createNotificationChannel() {
    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    )
    channel.description = NOTIFICATION_CHANNEL_DESCRIPTION

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
