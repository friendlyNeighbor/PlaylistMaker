package com.example.playlistmaker.mvvm.media.ui.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import com.example.playlistmaker.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.mvvm.uiCompose.ActionButton
import com.example.playlistmaker.mvvm.uiCompose.PlaceHolder

@Composable
fun PlaylistsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ActionButton(text = stringResource(R.string.new_playlist), modifier = Modifier.padding(top = 24.dp, bottom = 16.dp))

        PlaceHolder(
            R.drawable.ic_not_found_120,
            R.string.playlists_not_created,
            modifier = Modifier.padding(top = 46.dp)
            )
    }
}