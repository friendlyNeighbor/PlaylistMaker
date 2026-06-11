package com.example.playlistmaker.mvvm.player.ui

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor
) :
    ViewModel() {

    private val playerLiveData = MutableLiveData<PlayerState>()
    fun getLiveData(): LiveData<PlayerState> = playerLiveData

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private var timerJob: Job? = null

    private var playingStatus: PlayingStatus =PlayingStatus.DEFAULT
    private var playingTrack: Track = track
    private var playedTime:String =TIMER_ZERO
    private var isFavoriteTrack=false

    init {
        checkOnFavorite()
    }

    fun prepared() {
        preparePlayer()
    }

    private fun preparePlayer() {
            val url: String = track.previewUrl
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playingStatus = PlayingStatus.PREPARED
                postLiveData()
            }
            mediaPlayer.setOnCompletionListener {
                pausePlayer()
                playingStatus = PlayingStatus.PREPARED
                postLiveData()
            }
        }

    fun pause() {
        pausePlayer()
    }

    private fun pausePlayer() {
            mediaPlayer.pause()
            playingStatus = PlayingStatus.PAUSED
            stopTimer()
    }

    fun playbackControl() {
        if (playingStatus == PlayingStatus.PLAYING)
            pausePlayer()
        if (playingStatus == PlayingStatus.PREPARED || playingStatus == PlayingStatus.PAUSED)
                startPlayer()
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playingStatus = PlayingStatus.PLAYING
        postLiveData()
        runTimer()
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

    fun release() {
        stopTimer()
        mediaPlayer.release()
        playedTime = TIMER_ZERO
        playingStatus = PlayingStatus.DEFAULT
        postLiveData()
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun checkOnFavorite() {
            viewModelScope.launch {
               val list = favoritesInteractor.getFavoritesIdList().first()
                isFavoriteTrack = list.contains(track.trackId)
                    postLiveData()
            }
    }

    fun changeLike() {
        Log.d("MyError", "fun changeLike()")
        isFavoriteTrack = !isFavoriteTrack
        postLiveData()
    }

    fun refreshDataBase() {
        if(isFavoriteTrack) {
            favoritesInteractor.addTrackInFavorites(track)
        }
        else if(!isFavoriteTrack){
            favoritesInteractor.deleteTrackFromFavoritesById(track.trackId)
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
    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
        private const val TIMER_ZERO = "00:00"
    }
}