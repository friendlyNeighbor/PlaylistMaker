package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class AudioPlayerActivity : AppCompatActivity() {

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

        val buttonBack = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        buttonBack.setOnClickListener {
            finish()
        }

        val placeholder = findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.placeholder)

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

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).into(placeholder)
        viewTrackName.text = track.trackName
        viewArtistName.text = track.artistName
        viewTrackTime.text = track.trackTime
        viewYear.text = track.releaseDate.take(4)
        viewAlbum.text = track.collectionName
        viewGenre.text = track.primaryGenreName
        viewCountry.text = track.country

    }
}