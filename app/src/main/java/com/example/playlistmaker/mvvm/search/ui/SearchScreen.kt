package com.example.playlistmaker.mvvm.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.Header
import com.google.android.material.search.SearchBar
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(viewModel: SearchViewModel, modifier: Modifier = Modifier ) {

    // val state by viewModel.uiState.collectAsState() // LiveData/StateFlow → State

    Column(modifier = modifier.fillMaxSize()
        .background(MaterialTheme.colors.primary)
    ) {
        Header(stringResource(R.string.search))

    }
}