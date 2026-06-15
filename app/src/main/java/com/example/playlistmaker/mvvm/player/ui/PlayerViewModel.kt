package com.example.playlistmaker.mvvm.player.ui

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor,
    private val trackSaverInteractor: TrackSaverInteractor
) :
    ViewModel() {

    private val playerLiveData = MutableLiveData<PlayerState>()
    fun getLiveData(): LiveData<PlayerState> = playerLiveData
    private val playingTrack: Track = trackSaverInteractor.getTrackFromMemory()

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private var timerJob: Job? = null

    private var playingStatus: PlayingStatus = PlayingStatus.DEFAULT

    private var playedTime: String = TIMER_ZERO
    private var isFavoriteTrack = false

    init {
        checkOnFavorite()
    }

    fun prepared() {
        preparePlayer()
        if(playingTrack==null)
            Log.d("MyError", "null")
        Log.d("MyError", playingTrack.trackName)
    }

    private fun preparePlayer() {
        val url: String = playingTrack.previewUrl
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playingStatus = PlayingStatus.PREPARED
            postLiveData()
        }
        mediaPlayer.setOnCompletionListener {
            pausePlayer()
            playingStatus = PlayingStatus.PREPARED
            playedTime=TIMER_ZERO
            postLiveData()
        }
    }

    fun playbackControl() {
        when (playingStatus) {
            PlayingStatus.PREPARED, PlayingStatus.PAUSED ->  startPlayer()
            PlayingStatus.PLAYING                        ->  pausePlayer()
            PlayingStatus.DEFAULT                        ->  pausePlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playingStatus = PlayingStatus.PLAYING
        runTimer()
        postLiveData()
    }

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TIMER_UPDATE_DELAY.milliseconds)
                setTime()
            }
        }
    }

    private fun setTime() {
        playedTime = dateFormat.format(mediaPlayer.currentPosition)
        postLiveData()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playingStatus = PlayingStatus.PAUSED
        stopTimer()
        postLiveData()
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun release() {
        stopTimer()
        mediaPlayer.release()
        playedTime = TIMER_ZERO
        playingStatus = PlayingStatus.DEFAULT
        postLiveData()
    }

    fun checkOnFavorite() {
        viewModelScope.launch {
            val list = favoritesInteractor.getFavoritesIdList().first()
            isFavoriteTrack = list.contains(playingTrack.trackId)
            postLiveData()
        }
    }

    fun changeLike() {
        isFavoriteTrack = !isFavoriteTrack
        postLiveData()
    }

    fun refreshDataBase() {
        if (isFavoriteTrack) {
            favoritesInteractor.addTrackInFavorites(playingTrack)
        } else {
            favoritesInteractor.deleteTrackFromFavoritesById(playingTrack.trackId)
        }
    }

    private fun postLiveData() {
        playerLiveData.postValue(
            PlayerState(
                playingStatus,
                playingTrack,
                playedTime,
                isFavoriteTrack
            )
        )
    }

    fun getTrack(): Track {
        return playingTrack
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
        private const val TIMER_ZERO = "00:00"
    }
}
