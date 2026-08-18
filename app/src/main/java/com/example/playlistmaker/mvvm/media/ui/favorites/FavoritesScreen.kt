package com.example.playlistmaker.mvvm.media.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import com.example.playlistmaker.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playlistmaker.mvvm.uiCompose.PlaceHolder
import com.example.playlistmaker.mvvm.uiCompose.TrackItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = koinViewModel(), navController: NavController) {

    val uiState by viewModel.getLiveData().observeAsState()
    viewModel.setFavoritesLayout()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val favoritesList = uiState
        if (favoritesList.isNullOrEmpty()) {
            PlaceHolder(
                R.drawable.ic_not_found_120,
                R.string.mediateka_is_empty,
                modifier = Modifier.padding(top = 106.dp)
            )
        }
        else {
            LazyColumn(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                items(favoritesList) { track ->
                    TrackItem(track = track, {
                        viewModel.addTrackInMemory(track)
                        navController.navigate(R.id.action_mediatekaFragment_to_playerFragment)
                    } )
                }
            }
        }
    }
}