package com.example.playlistmaker.mvvm.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.ui.ActionPanel
import com.example.playlistmaker.mvvm.ui.Header
import com.example.playlistmaker.mvvm.ui.SwitchPanel


@Composable
fun SettingsScreen(modifier: Modifier = Modifier ) {

   // val state by viewModel.uiState.collectAsState() // LiveData/StateFlow → State

        Column(modifier = modifier.fillMaxSize()
        ) {
            Header(stringResource(R.string.settings))

            Spacer(Modifier.height(24.dp))

            SwitchPanel(stringResource(R.string.dark))

            ActionPanel(stringResource(R.string.share), R.drawable.ic_share_24)

            ActionPanel(stringResource(R.string.support), R.drawable.ic_support_24)

            ActionPanel(stringResource(R.string.agreement),R.drawable.ic_arrow_forward_24)

        }
    }


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(Modifier.padding(top = 25.dp))
}