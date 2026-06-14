package com.example.playlistmaker.mvvm.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.mvvm.media.ui.FragmentNewPlaylist
import com.example.playlistmaker.mvvm.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    // private val track: Track by lazy { requireArguments().get(TRACK) as Track }
    private val viewModel: PlayerViewModel by viewModel {
            parametersOf(track)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            savedInstanceState == null || savedInstanceState.isEmpty() -> {
                track = viewModel.getTrackListMemory()

            }
            else -> {
                track= requireArguments().get(TRACK) as Track
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .into(binding.placeholder)
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName.trim()
            valueTrackTime.text = track.trackTime
            valueYear.text = track.releaseDate.take(4)
            valueAlbum.text = track.collectionName
            valueGenre.text = track.primaryGenreName
            valueCountry.text = track.country

            buttonPlay.setOnClickListener {
                viewModel.playbackControl()
            }

            buttonLike.setOnClickListener {
                viewModel.changeLike()
            }

            buttonAdd.setOnClickListener {
                findNavController().navigate(
                    R.id.action_playerFragment_to_fragmentNewPlaylist,
                    FragmentNewPlaylist.createArgs(track)
                )
            }

            toolbar.setOnClickListener { findNavController().navigateUp() }
        } // binding apply

        viewModel.prepared()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            binding.apply {
                if (it.isFavoriteTrack)
                    buttonLike.setImageResource(R.drawable.ic_button_like_active_51)
                else
                    buttonLike.setImageResource(R.drawable.ic_button_like_51)

                if (it.playingStatus == PlayingStatus.PREPARED) {
                    buttonPlay.isEnabled = true
                    buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                    timer.text = it.playedTime
                }
                if (it.playingStatus == PlayingStatus.PLAYING) {
                    buttonPlay.setImageResource(R.drawable.ic_button_stop_100)
                    timer.text = it.playedTime
                }
                if (it.playingStatus == PlayingStatus.PAUSED) {
                    buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                }
                if (it.playingStatus == PlayingStatus.DEFAULT) {
                    buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                    buttonPlay.isEnabled = false
                }
            }
        }
    }

    override fun onPause() {
        viewModel.refreshDataBase()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
        viewModel.addTrackInMemory(track)
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
