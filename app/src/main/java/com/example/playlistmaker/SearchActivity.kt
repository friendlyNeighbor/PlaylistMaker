package com.example.playlistmaker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<Button>(R.id.search_back)

        buttonBack.setOnClickListener {
            finish()
        }
    }
}