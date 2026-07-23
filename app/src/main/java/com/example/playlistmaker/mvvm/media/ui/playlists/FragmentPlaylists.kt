package com.example.playlistmaker.mvvm.media.ui.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import com.example.playlistmaker.mvvm.media.ui.playlistScreen.FragmentPlaylistScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylists : Fragment() {
    private val viewModel: PlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val listOfPlaylists: MutableList<Playlist> = mutableListOf()
    private val playlistAdapter = PlaylistAdapter(listOfPlaylists)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter.onPlaylistClick = { playlist ->
            findNavController().navigate(R.id.action_mediatekaFragment_to_fragmentPlaylistScreen, FragmentPlaylistScreen.createArgs(playlist.id))
        }

        binding.recycler.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recycler.adapter = playlistAdapter

        binding.buttonCreate.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_fragmentNewPlaylist )
        }

        viewModel.readPlaylistDb()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            if (it.listOfPlaylists.isEmpty()) {
                binding.playlistsIsEmpty.visibility = View.VISIBLE
                binding.recycler.visibility = View.GONE
            }
            else {
                binding.playlistsIsEmpty.visibility = View.GONE
                listOfPlaylists.clear()
                listOfPlaylists.addAll(it.listOfPlaylists)
                playlistAdapter.notifyDataSetChanged()
                binding.recycler.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.readPlaylistDb()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentPlaylists()
    }

}