package com.example.playlistmaker.mvvm.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.mvvm.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel()

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

        track=viewModel.getTrack()
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
                    R.id.action_playerFragment_to_fragmentNewPlaylist
                )
            }

            toolbar.setOnClickListener { findNavController().navigateUp() }
        } // binding apply

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
