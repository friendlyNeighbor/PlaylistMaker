package com.example.playlistmaker.mvvm.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.uiCompose.ComposeTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : Fragment() {

    private val trackList: MutableList<Track> = mutableListOf()

    private val primaryState = SearchState(SearchStatus.CLEAR, trackList)
    private val viewModel: SearchViewModel by viewModel() {
        parametersOf(primaryState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeTheme(isSystemInDarkTheme()) {
                        SearchScreen(
                            viewModel = viewModel, navController
                            )
                }
            }
        }
    }
}
