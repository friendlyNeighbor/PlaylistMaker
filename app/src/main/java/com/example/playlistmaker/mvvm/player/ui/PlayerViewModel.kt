package com.example.playlistmaker.mvvm.player.ui

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(primaryState: PlayerState, private val mediaPlayer: MediaPlayer) :
    ViewModel() {

    private val playerLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<PlayerState> = playerLiveData

    private var timerJob: Job? = null

    private lateinit var time: String
    private var playerState = STATE_DEFAULT

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun prepared() {
        preparePlayer()
    }

    private fun preparePlayer() {
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            val url: String = currentState.playingTrack.previewUrl
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            time = TIMER_ZERO
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
                time = TIMER_ZERO
                pausePlayer()
                playerLiveData.postValue(
                    PlayerState(
                        PlayingStatus.PREPARED,
                        currentState.playingTrack,
                        TIMER_ZERO
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
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(250L)
                setTime()
            }
        }
    }

    private fun setTime() {
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            time = dateFormat.format(mediaPlayer.currentPosition)
            playerLiveData.postValue(
                PlayerState(
                    PlayingStatus.PLAYING,
                    currentState.playingTrack,
                    time
                )
            )
        }
    }

    fun release() {
        stopTimer()
        mediaPlayer.release()
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            time = TIMER_ZERO
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
        timerJob?.cancel()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private val TIMER_ZERO = "00:00"
    }
}