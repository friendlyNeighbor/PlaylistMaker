package com.example.playlistmaker.mvvm.player.ui

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.google.android.material.imageview.ShapeableImageView

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

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

        val placeholder = findViewById<ShapeableImageView>(R.id.placeholder)
        val viewTrackName = findViewById<TextView>(R.id.trackName)
        val viewArtistName = findViewById<TextView>(R.id.artistName)
        val viewTrackTime = findViewById<TextView>(R.id.valueTrackTime)
        val viewAlbum = findViewById<TextView>(R.id.valueAlbum)
        val viewYear = findViewById<TextView>(R.id.valueYear)
        val viewGenre = findViewById<TextView>(R.id.valueGenre)
        val viewCountry = findViewById<TextView>(R.id.valueCountry)
        val buttonBack = findViewById<Toolbar>(R.id.toolbar)

        buttonBack.setOnClickListener { finish() }

        val track: Track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK, Track::class.java) as Track
        } else {
            intent.getSerializableExtra(TRACK) as Track
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

        val buttonPlay = findViewById<ImageButton>(R.id.buttonPlay)
        val viewTimer = findViewById<TextView>(R.id.timer)

        val primaryState = PlayerState(PlayingStatus.DEFAULT, track, getString(R.string.timer))
        viewModel = ViewModelProvider(this, PlayerViewModel.Companion.getFactory(primaryState))
            .get(PlayerViewModel::class.java)

        viewModel.prepared()

        buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getLiveData().observe(this) {

            if(it.playingStatus == PlayingStatus.PREPARED) {
                buttonPlay.isEnabled = true
                buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                viewTimer.text = it.playedTime
            }
            if(it.playingStatus == PlayingStatus.PLAYING) {
                buttonPlay.setImageResource(R.drawable.ic_button_stop_100)
                viewTimer.text = it.playedTime
            }
            if(it.playingStatus == PlayingStatus.PAUSED) {
                buttonPlay.setImageResource(R.drawable.ic_button_play_100)
            }
            if(it.playingStatus == PlayingStatus.DEFAULT) {
                buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                buttonPlay.isEnabled = false
            }

        }
    } // <- onCreate

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    companion object {
        private const val TRACK = "TRACK"
    }
}