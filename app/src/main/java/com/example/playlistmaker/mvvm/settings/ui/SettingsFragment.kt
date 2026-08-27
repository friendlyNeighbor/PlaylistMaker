package com.example.playlistmaker.mvvm.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.mvvm.uiCompose.ComposeTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : Fragment() {

    private val primaryState = SettingsState.DEFAULT
    private val viewModel: SettingsViewModel by viewModel() {
        parametersOf(primaryState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                ComposeTheme(isSystemInDarkTheme()) {
                        SettingsScreen(
                            viewModel = viewModel
                        )
                }
            }
        }
    }
}
