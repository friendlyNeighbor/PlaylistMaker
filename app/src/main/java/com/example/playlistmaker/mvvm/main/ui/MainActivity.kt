package com.example.playlistmaker.mvvm.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.media.ui.MediatekaActivity
import com.example.playlistmaker.mvvm.search.ui.SearchActivity
import com.example.playlistmaker.mvvm.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonSettings = findViewById<Button>(R.id.button_settings)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSearch = findViewById<Button>(R.id.button_search)

        val primaryState = MainState.WAIT

        viewModel = ViewModelProvider(this,MainViewModel.getFactory(primaryState))
            .get(MainViewModel::class.java)

        viewModel.getLiveData().observe(this) {
            when {
                it == MainState.SEARCH -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    viewModel.resetState()
                }
                it == MainState.MEDIA -> {
                    startActivity(Intent(this, MediatekaActivity::class.java))
                    viewModel.resetState()
                }
                it == MainState.SETTINGS -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    viewModel.resetState()
                }
            }
        }

        buttonSearch.setOnClickListener {
            viewModel.goToSearchActivity()
        }

        buttonMedia.setOnClickListener {
            viewModel.goToMediatekaActivity()
        }

        buttonSettings.setOnClickListener {
            viewModel.goToSettingsActivity()
        }

    }
}
