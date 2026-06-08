package com.example.playlistmaker.mvvm.player.ui

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    primaryState: PlayerState,
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor
) :
    ViewModel() {

    private val playerLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<PlayerState> = playerLiveData

    private var timerJob: Job? = null

    private lateinit var time: String
    private var playerState = STATE_DEFAULT

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun prepared() {
        preparePlayer()
        checkOnFavorite()
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
                        time,
                        currentState.favoriteTrack
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
                        TIMER_ZERO,
                        currentState.favoriteTrack
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
                    time,
                    currentState.favoriteTrack
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
                    time,
                    currentState.favoriteTrack
                )
            )
            playerState = STATE_PLAYING
            runTimer()
        }
    }

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TIMER_UPDATE_DELAY)
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
                    time,
                    currentState.favoriteTrack
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
                    time,
                    currentState.favoriteTrack
                )
            )
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun insertToOrDeleteFromFavorites() {
        val currentState = playerLiveData.value
        if (currentState?.playingTrack != null) {
            val isFavorite = currentState.favoriteTrack
            if (isFavorite) {
                favoritesInteractor.deleteTrackFromFavorites(currentState.playingTrack)
            } else
                favoritesInteractor.addTrackInFavorites(currentState.playingTrack)

            checkOnFavorite()
        }

    }

    private fun checkOnFavorite() {
        val currentState = playerLiveData.value
        var isFavorite = false
        if (currentState?.playingTrack != null) {
            val track = currentState.playingTrack
            val id = track.trackId
            viewModelScope.launch {
                delay(300L)
                favoritesInteractor.getFavoritesIdList().collect { list ->
                    isFavorite = list.contains(id)
                }
                playerLiveData.postValue(
                    PlayerState(
                        currentState.playingStatus,
                        currentState.playingTrack,
                        currentState.playedTime,
                        isFavorite
                    )
                )
            }

        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_UPDATE_DELAY = 250L
        private const val TIMER_ZERO = "00:00"
    }
}