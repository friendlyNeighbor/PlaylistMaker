package com.example.playlistmaker.mvvm.settings.ui

import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        val primaryState = SettingsState.DEFAULT
        viewModel = ViewModelProvider(this, SettingsViewModel.Companion.getFactory(primaryState))
            .get(SettingsViewModel::class.java)

        val buttonBack = findViewById<Toolbar>(R.id.settings_back)
        buttonBack.setOnClickListener {
            finish()
        }

        val buttonShare = findViewById<TextView>(R.id.share)
        buttonShare.setOnClickListener {
            viewModel.share()
        }

        val buttonSupport = findViewById<TextView>(R.id.support)
        buttonSupport.setOnClickListener {
            viewModel.support()
        }

        val buttonAgreement = findViewById<TextView>(R.id.agreement)
        buttonAgreement.setOnClickListener {
            viewModel.agreement()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switch_dark_theme)
        themeSwitcher.setOnCheckedChangeListener { _, checked -> viewModel.switchTheme(checked) }

        viewModel.updateTheme()


        viewModel.getLiveData().observe(this) {
            if(it == SettingsState.DAY) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            if(it == SettingsState.NIGHT) {
                val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_YES
                if(!isDarkMode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeSwitcher.setChecked(true)
            }
        }

    }

}