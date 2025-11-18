package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<Toolbar>(R.id.settings_back)
        buttonBack.setOnClickListener {
            finish()
        }

        val buttonShare = findViewById<TextView>(R.id.share)
        buttonShare.setOnClickListener {
            val message = getString(R.string.link)
            val intent = Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            startActivity(intent)
        }
        val buttonSupport = findViewById<TextView>(R.id.support)
        buttonSupport.setOnClickListener {
            val text = getString(R.string.text_message)
            val subject = getString(R.string.subject_message)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_adress)))
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            startActivity(intent)
            }
        val buttonAgreement = findViewById<TextView>(R.id.agreement)
        buttonAgreement.setOnClickListener {
            val url = getString(R.string.link_offer)
            intent  = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
        }
    }
