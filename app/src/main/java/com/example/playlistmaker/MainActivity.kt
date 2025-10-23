package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/*class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Запускаем ПОИСК!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonSearch.setOnClickListener(imageClickListener)

        val buttonMedia = findViewById<Button>(R.id.button_media)
        buttonMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Открываем МЕДИАТЕКУ!", Toast.LENGTH_SHORT).show()
        }

        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Переходим в НАСТРОЙКИ!", Toast.LENGTH_SHORT).show()
        }

    }

}*/
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
            val displayIntent2 = Intent(this, Mediateka::class.java)
            startActivity(displayIntent2)
        }

        buttonSearch.setOnClickListener {
            val displayIntent3 = Intent(this, Search::class.java)
            startActivity(displayIntent3)
        }


        }
    }
