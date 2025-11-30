package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSettings = findViewById<Button>(R.id.button_settings)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSearch = findViewById<Button>(R.id.button_search)

        buttonSettings.setOnClickListener {
            val displayIntent1 = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent1)
        }

        buttonMedia.setOnClickListener {
            val displayIntent2 = Intent(this, MediatekaActivity::class.java)
            startActivity(displayIntent2)
        }

        buttonSearch.setOnClickListener {
            val displayIntent3 = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent3)
        }

        }
    }
