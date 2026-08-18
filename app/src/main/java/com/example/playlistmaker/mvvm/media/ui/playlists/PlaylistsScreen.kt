package com.example.playlistmaker.mvvm.media.ui.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import com.example.playlistmaker.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playlistmaker.mvvm.media.ui.playlistScreen.FragmentPlaylistScreen
import com.example.playlistmaker.mvvm.uiCompose.ActionButton
import com.example.playlistmaker.mvvm.uiCompose.PlaceHolder
import com.example.playlistmaker.mvvm.uiCompose.PlaylistItem

import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(viewModel: PlaylistsViewModel = koinViewModel(), navController: NavController) {

    val uiState by viewModel.getLiveData().observeAsState()
    viewModel.readPlaylistDb()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ActionButton(
            text = stringResource(R.string.new_playlist),
            { navController.navigate(R.id.action_mediatekaFragment_to_fragmentNewPlaylist ) },
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )

        if (uiState?.listOfPlaylists.isNullOrEmpty()) {
            PlaceHolder(
                R.drawable.ic_not_found_120,
                R.string.playlists_not_created,
                modifier = Modifier.padding(top = 46.dp)
            )
        } else {
            val playlists = uiState!!.listOfPlaylists

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(playlists) { playlist ->
                    PlaylistItem(playlist = playlist) {
                        navController.navigate(R.id.action_mediatekaFragment_to_fragmentPlaylistScreen, FragmentPlaylistScreen.createArgs(playlist.id))
                    }
                }
            }
        }
    }
}