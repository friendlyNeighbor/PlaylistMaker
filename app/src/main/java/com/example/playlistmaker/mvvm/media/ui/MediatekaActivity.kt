package com.example.playlistmaker.mvvm.media.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediatekaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediatekaBinding
    private val primaryState = false
    private val viewModel: MediatekaViewModel by viewModel() {
        parametersOf(primaryState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mediateka)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.mediaBack.setOnClickListener {
            viewModel.finishActivity()
        }
        viewModel.getLiveData().observe(this) {
            if(it)
                finish()
        }
    }
}
