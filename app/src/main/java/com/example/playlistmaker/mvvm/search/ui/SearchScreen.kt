package com.example.playlistmaker.mvvm.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
//import androidx.compose.material3.SearchBar
//import androidx.compose.material3.SearchBarDefaults
//import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.uiCompose.ActionButton
import com.example.playlistmaker.mvvm.uiCompose.Header
import com.example.playlistmaker.mvvm.uiCompose.SearchField
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.placeholderStyle
import com.example.playlistmaker.mvvm.uiCompose.TrackItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val trackList: MutableList<Track> = mutableListOf()
private val trackListHistory: MutableList<Track> = mutableListOf()
//var isClickAllowed = true

//private const val TEXT_DEFAULT = ""
private const val CLICK_DEBOUNCE_DELAY = 1000L


@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {

    // val state by viewModel.uiState.collectAsState() // LiveData/StateFlow → State
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
            isSystemInDarkTheme(),
            { text -> viewModel.textWasChanged(text) },
            { focus -> viewModel.editTextInFocus(focus) }
            )

        Spacer(modifier = Modifier.height(16.dp))

        if(uiState?.searchStatus == SearchStatus.HISTORY) {
            trackListHistory.clear()
            uiState?.searchResult?.let { trackListHistory.addAll(it) } }
            else {
                trackList.clear()
                uiState?.searchResult?.let { trackList.addAll(it) }
            }

        when (uiState?.searchStatus) {

            SearchStatus.CONNECTION_PROBLEM -> {
                Image(
                    modifier = Modifier
                        .padding(top = 102.dp, bottom = 16.dp),
                    painter = painterResource(R.drawable.ic_connection_problem_120),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .width(312.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.connection_problem),
                    style = placeholderStyle(),
                    color = MaterialTheme.colorScheme.onPrimary)
                ActionButton(text = stringResource(R.string.reload),  {viewModel.textWasChanged(viewModel.text+" ")} )
            }

            SearchStatus.NOT_FOUND -> {
                Image(
                    modifier = Modifier
                        .padding(top = 102.dp, bottom = 16.dp),
                    painter = painterResource(R.drawable.ic_not_found_120),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.not_found),
                    textAlign = TextAlign.Center,
                    style = placeholderStyle(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            SearchStatus.SEARCH_SUCCESSFUL -> {
                LazyColumn { items(trackList) {track -> TrackItem(track=track, {
                    if (clickDebounce()) {
                        viewModel.addTrackInHistory(track)
                        viewModel.addTrackInMemory(track)
                        navController.navigate(R.id.action_searchFragment_to_playerFragment )
                    }
                }
                )}}
            }

            SearchStatus.HISTORY -> {
                Text(
                    text = stringResource(R.string.history_of_search),
                    style = placeholderStyle(),
                    modifier = Modifier
                        .padding(top = 42.dp, bottom = 20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                LazyColumn(modifier = Modifier.weight(0.9f, fill = false)) {
                    items(trackListHistory) {track -> TrackItem(track=track, {
                        if (clickDebounce()) {
                            viewModel.addTrackInHistory(track)
                            viewModel.addTrackInMemory(track)
                            navController.navigate(R.id.action_searchFragment_to_playerFragment )
                        }
                    }
                    )}
                }

                Spacer(modifier = Modifier.height(24.dp))

                ActionButton(text = stringResource(R.string.clear_history),  { viewModel.clearHistory() } )
            }

            SearchStatus.PROGRESS -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(44.dp)
                        .offset(y = 140.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = Color.Transparent,
                    strokeCap = StrokeCap.Butt
                )
            }
            SearchStatus.CLEAR -> {}//binding.clearSearch.visibility = View.GONE
            null -> {}
        }

/*
    viewModel.getLiveData().observe(viewLifecycleOwner) {
        if(uiState.searchStatus == SearchStatus.HISTORY) {
            trackListHistory.clear()
            trackListHistory.addAll(it.searchResult)
        }
        else {
            trackList.clear()
            trackList.addAll(it.searchResult)
        }
        setViewSearch(it.searchStatus)
    }

        fun setViewSearch(reason: SearchStatus) {
      //  tracksAdapter.notifyDataSetChanged()
      //  historyAdapter.notifyDataSetChanged()
      */

/*
        binding.apply {
            notFound.visibility = View.GONE
            connectionProblem.visibility = View.GONE
            recycler.visibility = View.GONE
            historyOfSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearSearch.visibility = View.VISIBLE
        }
        when (reason) {
            SearchStatus.CONNECTION_PROBLEM -> binding.connectionProblem.visibility = View.VISIBLE
            SearchStatus.NOT_FOUND -> binding.notFound.visibility = View.VISIBLE
            SearchStatus.SEARCH_SUCCESSFUL -> binding.recycler.visibility = View.VISIBLE
            SearchStatus.HISTORY -> {
                binding.historyOfSearch.visibility = View.VISIBLE
                binding.clearSearch.visibility = View.GONE
            }
            SearchStatus.PROGRESS ->  binding.progressBar.visibility = View.VISIBLE
            SearchStatus.CLEAR ->  binding.clearSearch.visibility = View.GONE
        }

 */
    }


}
/*
@Composable
fun SetViewSearch(reason: SearchStatus?) {
    //  tracksAdapter.notifyDataSetChanged()
    //  historyAdapter.notifyDataSetChanged()

    /*
                binding.apply {
                    notFound.visibility = View.GONE
                    connectionProblem.visibility = View.GONE
                    recycler.visibility = View.GONE
                    historyOfSearch.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    clearSearch.visibility = View.VISIBLE
                }

     */


}
*/
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


