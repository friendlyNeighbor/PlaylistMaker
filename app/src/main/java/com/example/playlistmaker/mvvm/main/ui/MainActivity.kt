package com.example.playlistmaker.mvvm.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.mvvm.media.ui.MediatekaActivity
import com.example.playlistmaker.mvvm.search.ui.SearchActivity
import com.example.playlistmaker.mvvm.settings.ui.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val primaryState = MainState.WAIT
    private val viewModel: MainViewModel by viewModel() {
        parametersOf(primaryState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        binding.buttonSearch.setOnClickListener {
            viewModel.goToSearchActivity()
        }

        binding.buttonMedia.setOnClickListener {
            viewModel.goToMediatekaActivity()
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.goToSettingsActivity()
        }

    }
}
