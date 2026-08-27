package com.example.playlistmaker.mvvm.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.components.ActionPanel
import com.example.playlistmaker.mvvm.uiCompose.components.Header
import com.example.playlistmaker.mvvm.uiCompose.components.SwitchPanel


@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier ) {

        Column(modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
        ) {
            Header(
                stringResource(R.string.settings),
                modifier = Modifier.padding(bottom = 24.dp))

            SwitchPanel(stringResource(R.string.dark), {viewModel.switchTheme()}, viewModel.getTheme() )

            ActionPanel(stringResource(R.string.share), R.drawable.ic_share_24, {viewModel.share()})

            ActionPanel(stringResource(R.string.support), R.drawable.ic_support_24, {viewModel.support()})

            ActionPanel(stringResource(R.string.agreement),R.drawable.ic_arrow_forward_24, {viewModel.agreement()})
        }
    }
