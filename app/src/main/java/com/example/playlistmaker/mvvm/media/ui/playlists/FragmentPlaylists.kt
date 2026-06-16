package com.example.playlistmaker.mvvm.media.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylists : Fragment() {
    private val viewModel: PlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreate.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_fragmentNewPlaylist )
        }

        viewModel.playlistsIsEmpty()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            if (it == PlaylistsState.EMPTY)
                binding.playlistsIsEmpty.visibility= View.VISIBLE
            else
                binding.playlistsIsEmpty.visibility= View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentPlaylists()
    }

}