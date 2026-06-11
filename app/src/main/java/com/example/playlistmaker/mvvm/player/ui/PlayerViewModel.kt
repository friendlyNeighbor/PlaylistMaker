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

    //private var timerJob: Job? = null

   // private lateinit var time: String

    private var isFavorite=false
    init {
        checkOnFavorite()
    }

   // private var playerState = STATE_DEFAULT // ?
   // val currentState by lazy { playerLiveData.value }
   val currentState = PlayerState(PlayingStatus.DEFAULT, track, TIMER_ZERO, false)

/*
    fun prepared() {
        preparePlayer()
        //checkOnFavorite()
        Log.d("MyError", "fun prepared()")
        Log.d("MyError", "PlayingStatus = ${playerLiveData.value?.playingStatus}")
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
        Log.d("MyError", "fun pause()")
        Log.d("MyError", "PlayingStatus = ${playerLiveData.value?.playingStatus}")
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
        Log.d("MyError", "fun playbackControl()")
        Log.d("MyError", "PlayingStatus = ${playerLiveData.value?.playingStatus}")
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
                delay(TIMER_UPDATE_DELAY.milliseconds)
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
        Log.d("MyError", "fun release()")
        Log.d("MyError", "PlayingStatus = ${playerLiveData.value?.playingStatus}")
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
        Log.d("MyError", "fun insertorDelete()")
        Log.d("MyError", "PlayingStatus = ${playerLiveData.value?.playingStatus}")
    }
*/
    fun checkOnFavorite() {
            viewModelScope.launch {
               val list = favoritesInteractor.getFavoritesIdList().first()
                    isFavorite = list.contains(track.trackId)
                    setLike()
            }
    }

    fun changeLike() {
        Log.d("MyError", "fun changeLike()")
        isFavorite = !isFavorite
        setLike()
    }

    private fun setLike(){
        if(currentState!=null) {
        playerLiveData.postValue(
            PlayerState(
                currentState.playingStatus,
                currentState.playingTrack,
                currentState.playedTime,
                isFavorite
            )
        )}
    }
    fun refreshDataBase() {
        if(isFavorite) {
            favoritesInteractor.addTrackInFavorites(track)
            Log.d("MyError", "fun refreshDataBase() - addTrackInFavorites(track)      ${track.number} ${track.trackId} ${track.trackName} ${track.artistName} ${track.trackTime} ${track.artworkUrl100} ${track.artworkUrl100} ${track.collectionName} ${track.releaseDate} ${track.primaryGenreName} ${track.country} ${track.previewUrl}")
            Log.d("MyError", "fun refreshDataBase() - isFavorite = $isFavorite")
        }
        else if(!isFavorite){
            favoritesInteractor.deleteTrackFromFavoritesById(track.trackId)
            Log.d("MyError", "fun refreshDataBase() - deleteTrackFromFavorites(track) ${track.number} ${track.trackId} ${track.trackName} ${track.artistName} ${track.trackTime} ${track.artworkUrl100} ${track.artworkUrl100} ${track.collectionName} ${track.releaseDate} ${track.primaryGenreName} ${track.country} ${track.previewUrl}")
            Log.d("MyError", "fun refreshDataBase() - isFavorite = $isFavorite")
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_UPDATE_DELAY = 300L
        private const val TIMER_ZERO = "00:00"
    }
}