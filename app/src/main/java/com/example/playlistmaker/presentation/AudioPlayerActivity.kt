package com.example.playlistmaker.presentation

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.imageview.ShapeableImageView
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var buttonPlay: ImageButton
    private lateinit var viewTimer: TextView

    private lateinit var time: String
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var url: String

    val handler = Handler(Looper.getMainLooper())
    val runSetTime = Runnable { setTime() }
    fun setTime() {
        time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        viewTimer.text = time
        runTimer()
    }

    private fun runTimer() {
        handler.postDelayed(runSetTime, 250)
    }

    private fun stopTimer() {
        handler.removeCallbacks(runSetTime)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            pausePlayer()
            time = "00:00"
            viewTimer.text = time
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        buttonPlay.setImageResource(R.drawable.ic_button_stop_100)
        playerState = STATE_PLAYING
        runTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        buttonPlay.setImageResource(R.drawable.ic_button_play_100)
        playerState = STATE_PAUSED
        stopTimer()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonBack = findViewById<Toolbar>(R.id.toolbar)
        buttonBack.setOnClickListener {
            finish()
        }

        val placeholder =
            findViewById<ShapeableImageView>(R.id.placeholder)

        val viewTrackName = findViewById<TextView>(R.id.trackName)
        val viewArtistName = findViewById<TextView>(R.id.artistName)
        val viewTrackTime = findViewById<TextView>(R.id.valueTrackTime)
        val viewAlbum = findViewById<TextView>(R.id.valueAlbum)
        val viewYear = findViewById<TextView>(R.id.valueYear)
        val viewGenre = findViewById<TextView>(R.id.valueGenre)
        val viewCountry = findViewById<TextView>(R.id.valueCountry)

        val track: Track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("track", Track::class.java) as Track
        } else {
            intent.getSerializableExtra("track") as Track
        }

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .into(placeholder)
        viewTrackName.text = track.trackName
        viewArtistName.text = track.artistName
        viewTrackTime.text = track.trackTime
        viewYear.text = track.releaseDate.take(4)
        viewAlbum.text = track.collectionName
        viewGenre.text = track.primaryGenreName
        viewCountry.text = track.country
        url = track.previewUrl
        buttonPlay = findViewById(R.id.buttonPlay)
        viewTimer = findViewById(R.id.timer)

        preparePlayer()

        buttonPlay.setOnClickListener {
            playbackControl()
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}