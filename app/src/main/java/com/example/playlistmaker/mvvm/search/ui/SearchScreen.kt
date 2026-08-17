package com.example.playlistmaker.mvvm.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.uiCompose.ActionButton
import com.example.playlistmaker.mvvm.uiCompose.Header
import com.example.playlistmaker.mvvm.uiCompose.PlaceHolder
import com.example.playlistmaker.mvvm.uiCompose.SearchField
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.placeholderStyle
import com.example.playlistmaker.mvvm.uiCompose.TrackItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val trackList: MutableList<Track> = mutableListOf()
private val trackListHistory: MutableList<Track> = mutableListOf()
private const val CLICK_DEBOUNCE_DELAY = 1000L


@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {

    val clickDebounce = rememberClickDebounce()
    val uiState by viewModel.getLiveData().observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(stringResource(R.string.search))

        SearchField(
            { text -> viewModel.textWasChanged(text) },
            { focusState -> viewModel.focusWasChanged(focusState) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (uiState?.searchStatus == SearchStatus.HISTORY) {
            trackListHistory.clear()
            uiState?.searchResult?.let { trackListHistory.addAll(it) }
        } else {
            trackList.clear()
            uiState?.searchResult?.let { trackList.addAll(it) }
        }

        if (uiState?.searchStatus == SearchStatus.CONNECTION_PROBLEM) {
            PlaceHolder(
                R.drawable.ic_connection_problem_120,
                R.string.connection_problem,
                modifier = Modifier.padding(top = 102.dp)
            )
        }

        if (uiState?.searchStatus == SearchStatus.NOT_FOUND) {
            PlaceHolder(
                R.drawable.ic_not_found_120,
                R.string.not_found,
                modifier = Modifier.padding(top = 102.dp)
            )
        }

        if (uiState?.searchStatus == SearchStatus.SEARCH_SUCCESSFUL) {
            LazyColumn {
                items(trackList) { track ->
                    TrackItem(track = track, {
                        if (clickDebounce()) {
                            viewModel.addTrackInHistory(track)
                            viewModel.addTrackInMemory(track)
                            navController.navigate(R.id.action_searchFragment_to_playerFragment)
                        }
                    }
                    )
                }
            }
        }

        if (uiState?.searchStatus == SearchStatus.HISTORY) {
            Text(
                text = stringResource(R.string.history_of_search),
                style = placeholderStyle(),
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )

            LazyColumn(modifier = Modifier.weight(0.9f, fill = false)) {
                items(trackListHistory) { track ->
                    TrackItem(track = track, {
                        if (clickDebounce()) {
                            viewModel.addTrackInHistory(track)
                            viewModel.addTrackInMemory(track)
                            navController.navigate(R.id.action_searchFragment_to_playerFragment)
                        }
                    }
                    )
                }
            }

            ActionButton(
                text = stringResource(R.string.clear_history),
                { viewModel.clearHistory() },
                modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
            )
        }

        if (uiState?.searchStatus == SearchStatus.PROGRESS) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(44.dp)
                    .offset(y = 140.dp),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = Color.Transparent,
                strokeCap = StrokeCap.Butt
            )
        }

    }

}

@Composable
fun rememberClickDebounce(): () -> Boolean {
    var isClickAllowed by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    return remember {
        {
            val current = isClickAllowed

            if (isClickAllowed) {
                isClickAllowed = false

                scope.launch {
                    delay(CLICK_DEBOUNCE_DELAY)
                    isClickAllowed = true
                }
            }
            current
        }
    }
}


