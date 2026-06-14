package com.example.playlistmaker.mvvm.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.mvvm.search.domain.model.Track


class FragmentNewPlaylist: Fragment() {
    //private val viewModel: FavoritesViewModel by viewModel()

    private val track: Track by lazy { requireArguments().get(TRACK) as Track }
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener { findNavController().navigateUp() }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TRACK = "TRACK"

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to track)
    }
}