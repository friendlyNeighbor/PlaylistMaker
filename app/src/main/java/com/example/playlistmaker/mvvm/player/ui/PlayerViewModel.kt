package com.example.playlistmaker.mvvm.player.ui

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
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
    private val trackSaverInteractor: TrackSaverInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val imageSaverInteractor: ImageSaverInteractor,
    private val favoritesInteractor2: FavoritesInteractor,
) :
    ViewModel() {

    private val playerLiveData = MutableLiveData<PlayerState>()
    fun getLiveData(): LiveData<PlayerState> = playerLiveData

    private val playingTrack: Track = getTrack()
    private var playingStatus: PlayingStatus = PlayingStatus.DEFAULT
    private var playedTime: String = TIMER_ZERO
    private var isFavoriteTrack = false
    private var listOfPlaylist: List<Playlist> = emptyList()

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var timerJob: Job? = null

    init {
        checkOnFavorite()
        preparePlayer()
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

    private fun release() {
        stopTimer()
        mediaPlayer.release()
        playedTime = TIMER_ZERO
        playingStatus = PlayingStatus.DEFAULT
        postLiveData()
    }

    private fun checkOnFavorite() {
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

    fun  readPlaylistDb() {
        viewModelScope.launch {
            val list = playlistInteractor.getListOfPlaylists().first()
            if (list.isNotEmpty()) {
                    for (playlist in list) {
                        playlist.uriImage = imageSaverInteractor.getImage(playlist.title)
                    }
                listOfPlaylist = list
                postLiveData()
            }
        }
    }

    private fun postLiveData() {
        playerLiveData.postValue(
            PlayerState(
                playingStatus,
                playingTrack,
                playedTime,
                isFavoriteTrack,
                listOfPlaylist
            )
        )
    }

    fun getTrack(): Track {
        return trackSaverInteractor.getTrackFromMemory()
    }



    override fun onCleared() {
        super.onCleared()
        release()
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
        private const val TIMER_ZERO = "00:00"
    }
}
