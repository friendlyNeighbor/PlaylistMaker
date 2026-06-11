package com.example.playlistmaker.mvvm.media.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.mvvm.player.ui.PlayerFragment
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class FragmentFavorites : Fragment() {
    private val viewModel: FavoritesViewModel by viewModel()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val trackListOfFavorites: MutableList<Track> = mutableListOf()
    private val tracksAdapter = TrackAdapter(trackListOfFavorites)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = tracksAdapter
        tracksAdapter.onTrackClick = { track ->
             findNavController().navigate(
                    R.id.action_mediatekaFragment_to_playerFragment,
                    PlayerFragment.createArgs(track))

        }

        viewModel.setFavoritesLayout()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.favoritesIsEmpty.visibility = VISIBLE
                binding.recycler.visibility = GONE
            }
            else {
                trackListOfFavorites.clear()
                trackListOfFavorites.addAll(it)
                tracksAdapter.notifyDataSetChanged()
                binding.favoritesIsEmpty.visibility = GONE
                binding.recycler.visibility = VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentFavorites()
    }

}
