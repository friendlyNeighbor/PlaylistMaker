package com.example.playlistmaker.mvvm.player.domain

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.mvvm.player.domain.PlayingStatus
import java.util.Locale

class PlayerViewModel(primaryState: PlayerState) : ViewModel() {

    private val playerLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<PlayerState> = playerLiveData

    val handler = Handler(Looper.getMainLooper())

    private var mediaPlayer = MediaPlayer()

    private lateinit var time: String
    private var playerState = STATE_DEFAULT

    fun prepared() {
        preparePlayer()
    }
    private fun preparePlayer() {
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            val url: String = currentState.playingTrack.previewUrl
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            time="00:00"
            mediaPlayer.setOnPreparedListener {
                playerLiveData.postValue(
                    PlayerState(
                        PlayingStatus.PREPARED,
                        currentState.playingTrack,
                        time
                    )
                )
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                time="00:00"
                pausePlayer()
                playerLiveData.postValue(
                    PlayerState(
                        PlayingStatus.PREPARED,
                        currentState.playingTrack,
                        "00:00"
                    )
                )
            }
        }
    }


    fun pause() {
        pausePlayer()
    }
    private fun pausePlayer() {
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            mediaPlayer.pause()
            playerLiveData.postValue(
                PlayerState(
                    PlayingStatus.PAUSED,
                    currentState.playingTrack,
                    time
                )
            )
            playerState = STATE_PAUSED
            stopTimer()
        }
    }


    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            mediaPlayer.start()
            playerLiveData.postValue(
                PlayerState(
                    PlayingStatus.PLAYING,
                    currentState.playingTrack,
                    time
                )
            )
            playerState = STATE_PLAYING
            runTimer()
        }
    }


    private fun runTimer() {
        handler.postDelayed(runSetTime, 250)
    }

    private val runSetTime = Runnable { setTime() }

    private fun setTime() {
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            playerLiveData.postValue(
                PlayerState(
                    PlayingStatus.PLAYING,
                    currentState.playingTrack,
                    time
                )
            )
            runTimer()
        }
    }


    fun release() {
        stopTimer()
        mediaPlayer.release()
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            time="00:00"
            playerLiveData.postValue(
                PlayerState(
                    PlayingStatus.DEFAULT,
                    currentState.playingTrack,
                    time
                )
            )
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(runSetTime)
    }


    companion object {
        fun getFactory(value: PlayerState): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(value)
            }
        }
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}