package com.example.playlistmaker.mvvm.media.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.media.ui.favorites.FavoritesScreen
import com.example.playlistmaker.mvvm.media.ui.playlists.PlaylistsScreen
import com.example.playlistmaker.mvvm.uiCompose.components.Header
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.buttonStyle
import kotlinx.coroutines.launch

@Composable
fun MediatekaScreen( navController: NavController ) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = pagerState.currentPage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(48.dp)
    ) {
        Header(stringResource(R.string.mediateka))

        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(selectedTabIndex, matchContentSize = false)
                        .width(48.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            divider = {}
            ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text(text = stringResource(R.string.favorites),
                    style = buttonStyle()) },
            )

            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text(text = stringResource(R.string.playlists),
                    style = buttonStyle()) },
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when(page) {
                0 -> FavoritesScreen(navController = navController)
                1 -> PlaylistsScreen(navController = navController)
            }
        }
    }
}