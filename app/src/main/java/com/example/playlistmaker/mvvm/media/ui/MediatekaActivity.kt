package com.example.playlistmaker.mvvm.media.ui

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediatekaActivity : AppCompatActivity() {

    private val primaryState = false
    private val viewModel: MediatekaViewModel by viewModel() {
        parametersOf(primaryState)
    }

//    private lateinit var viewModel: MediatekaViewModel
    private lateinit var button: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_mediateka)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mediateka)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
/*
        val primaryState = false
        viewModel = ViewModelProvider(this,MediatekaViewModel.getFactory(primaryState))
            .get(MediatekaViewModel::class.java)


 */
        viewModel.getLiveData().observe(this) {
            if(it)
                finish()
        }

        button = findViewById(R.id.media_back)
        button.setOnClickListener {
            viewModel.finishActivity()
        }
    }
}