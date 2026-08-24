package com.example.playlistmaker.mvvm.media.ui.createPlaylist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.mvvm.uiCompose.ComposeTheme


class FragmentCreatePlaylist : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()

        return ComposeView(requireContext()).apply {
            setContent {
                ComposeTheme(isSystemInDarkTheme()) {
                    CreatePlaylistScreen( navController )
                }
            }
        }
    }
}
