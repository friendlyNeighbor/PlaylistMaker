package com.example.playlistmaker.mvvm.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.ActionPanel
import com.example.playlistmaker.mvvm.uiCompose.Header
import com.example.playlistmaker.mvvm.uiCompose.SwitchPanel


@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier ) {

   // val state by viewModel.uiState.collectAsState() // LiveData/StateFlow → State

        Column(modifier = modifier.fillMaxSize()
        ) {
            Header(stringResource(R.string.settings))

            Spacer(Modifier.height(24.dp))

            SwitchPanel(stringResource(R.string.dark))
            SwitchPanel("Тест", {viewModel.switchTheme()}) //

            ActionPanel(stringResource(R.string.share), R.drawable.ic_share_24, {viewModel.share()})

            ActionPanel(stringResource(R.string.support), R.drawable.ic_support_24, {viewModel.support()})

            ActionPanel(stringResource(R.string.agreement),R.drawable.ic_arrow_forward_24, {viewModel.agreement()})

        }
    }

/*
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(Modifier.padding(top = 25.dp))
}

 */