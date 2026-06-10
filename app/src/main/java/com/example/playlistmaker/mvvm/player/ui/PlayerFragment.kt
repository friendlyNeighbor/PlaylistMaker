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
import com.example.playlistmaker.mvvm.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
 //   private lateinit var primaryState: PlayerState
 //   private val viewModel: PlayerViewModel by viewModel() {
 //       parametersOf(primaryState)
 //   }
    private val track: Track by lazy { requireArguments().get(TRACK) as Track }
    private val viewModel: PlayerViewModel by viewModel() {
            parametersOf(track)
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


      //  val track: Track = requireArguments().get(TRACK) as Track

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
/*
            buttonPlay.setOnClickListener {
                viewModel.playbackControl()
                Log.d("MyError", "PLAY button")
            }
*/
            buttonLike.setOnClickListener {
                viewModel.changeLike()
                Log.d("MyError", "LIKE button")
            }

            toolbar.setOnClickListener { findNavController().navigateUp() }
        } // binding apply
      //  primaryState = PlayerState(PlayingStatus.DEFAULT, track, getString(R.string.timer), false)
        /*
        viewModel.prepared()
        Log.d("MyError", "Frag:prepared")

         */
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            Log.d("MyError", "Frag:observe")

            binding.apply {
                if (it.favoriteTrack==true)
                    buttonLike.setImageResource(R.drawable.ic_button_like_active_51)
                else
                    buttonLike.setImageResource(R.drawable.ic_button_like_51)
/*
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
*/

            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.refreshDataBase()
    //    viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
    //    viewModel.release()
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