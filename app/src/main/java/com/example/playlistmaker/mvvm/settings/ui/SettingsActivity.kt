package com.example.playlistmaker.mvvm.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val primaryState = SettingsState.DEFAULT
    private val viewModel: SettingsViewModel by viewModel() {
        parametersOf(primaryState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        binding.apply {
            settingsBack.setOnClickListener {
                finish()
            }
            share.setOnClickListener {
                viewModel.share()
            }
            support.setOnClickListener {
                viewModel.support()
            }
            agreement.setOnClickListener {
                viewModel.agreement()
            }
            switchDarkTheme.setOnClickListener {
                viewModel.switchTheme()
            }
        }

        viewModel.updateSwitcher()

        viewModel.getLiveData().observe(this) {
            if(it == SettingsState.NIGHT && !binding.switchDarkTheme.isChecked)
                binding.switchDarkTheme.setChecked(true)
        }
    }
}
