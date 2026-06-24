package com.example.playlistmaker.mvvm.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel()

    private val listOfPlaylist: MutableList<Playlist> = mutableListOf()
    private val playerAdapter = PlayerAdapter(listOfPlaylist)
    private var pokedPlaylistTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        playerAdapter.onPlaylistClick = { playlist ->
            pokedPlaylistTitle = playlist.title
            viewModel.addTrackInSorted()
            viewModel.addTrackIdInPlaylist(playlist)
        }

        binding.recycler.adapter = playerAdapter

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
                viewModel.readPlaylistDb()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            toolbar.setOnClickListener { findNavController().navigateUp() }

            buttonCreate.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_fragmentNewPlaylist)
            }
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
                if(it.listOfPlaylist.isNotEmpty()) {
                    listOfPlaylist.clear()
                    listOfPlaylist.addAll(it.listOfPlaylist)
                    playerAdapter.notifyDataSetChanged()
                }
                if(it.isInPlaylistYet==true)
                    Toast.makeText(requireActivity(),"Трек уже добавлен в плейлист $pokedPlaylistTitle", Toast.LENGTH_LONG).show()
                if(it.isInPlaylistYet==false) {
                    Toast.makeText(requireActivity(),"Добавлено в плейлист $pokedPlaylistTitle", Toast.LENGTH_LONG).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    override fun onPause() {
        viewModel.refreshDataBase()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.readPlaylistDb()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
