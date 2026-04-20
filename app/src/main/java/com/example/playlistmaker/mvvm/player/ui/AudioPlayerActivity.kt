package com.example.playlistmaker.mvvm.player.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.mvvm.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var primaryState:PlayerState
    private val viewModel: PlayerViewModel by viewModel() {
        parametersOf(primaryState)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val track: Track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK, Track::class.java) as Track
        } else {
            intent.getSerializableExtra(TRACK) as Track
        }

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .into(binding.placeholder)
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            valueTrackTime.text = track.trackTime
            valueYear.text = track.releaseDate.take(4)
            valueAlbum.text = track.collectionName
            valueGenre.text = track.primaryGenreName
            valueCountry.text = track.country

            buttonPlay.setOnClickListener {
                viewModel.playbackControl()
            }
            toolbar.setOnClickListener { finish() }
        }
        primaryState = PlayerState(PlayingStatus.DEFAULT, track, getString(R.string.timer))
        viewModel.prepared()

        viewModel.getLiveData().observe(this) {
            binding.apply {
                if (it.playingStatus == PlayingStatus.PREPARED) {
                    binding.buttonPlay.isEnabled = true
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                    binding.timer.text = it.playedTime
                }
                if (it.playingStatus == PlayingStatus.PLAYING) {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_stop_100)
                    binding.timer.text = it.playedTime
                }
                if (it.playingStatus == PlayingStatus.PAUSED) {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                }
                if (it.playingStatus == PlayingStatus.DEFAULT) {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                    binding.buttonPlay.isEnabled = false
                }
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