package com.example.playlistmaker.mvvm.media.ui.playlistScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.mvvm.media.ui.createPlaylist.FragmentCreatePlaylist
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class FragmentPlaylistScreen: Fragment() {
    private val viewModel: ViewModelPlaylistScreen by viewModel()

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private val trackList: MutableList<Track> = mutableListOf()
    private val playlistAdapter = TrackAdapter(trackList)

    lateinit var confirmDialog: MaterialAlertDialogBuilder

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
    return binding.root
}

@SuppressLint("NotifyDataSetChanged")
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val id: Long = requireArguments().getLong(ID)

    val bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.standardBottomSheetMenu)
    bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
    binding.recycler.adapter = playlistAdapter

    playlistAdapter.onTrackClick = { track ->
        viewModel.addTrackInMemory(track)
        findNavController().navigate(R.id.action_fragmentPlaylistScreen_to_playerFragment )
    }

    playlistAdapter.onTrackLongClick = { track ->
        showDialogDeleteTrack(track)
    }

    binding.toolbar.setOnClickListener {
        findNavController().navigateUp()
    }

    binding.sharePlaylist.setOnClickListener {
        sharingPlaylist()
    }

    binding.menu.setOnClickListener {
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    binding.share.setOnClickListener {
        sharingPlaylist()
    }

    binding.edit.setOnClickListener {
        findNavController().navigate(
            R.id.action_fragmentPlaylistScreen_to_fragmentNewPlaylist,
            FragmentCreatePlaylist.createArgs(id)
        )
    }

    binding.deletePlaylist.setOnClickListener {
        showDialogDeletePlaylist()
    }

    viewModel.loadPlaylistById(id)

    viewModel.getLiveData().observe(viewLifecycleOwner) {
        if (it.playlistDeleted)
            findNavController().navigateUp()
        else {
            if(it.trackList.isEmpty()) {
                binding.haveNoTracks.visibility = View.VISIBLE
                binding.standardBottomSheet.visibility = View.GONE
            }

            binding.title.text = it.title
            binding.includedPlaylistView.title.text = it.title
            binding.description.text = it.description
            binding.quantity.text =
                if (it.quantity == 0) {
                    requireActivity().resources.getString(R.string.no_tracks)
                } else {
                    requireActivity().resources.getQuantityString(
                        R.plurals.track_count,
                        it.quantity,
                        it.quantity
                    )
                }
            binding.includedPlaylistView.numberOfTracks.text = binding.quantity.text
            binding.duration.text =
                if (it.duration == 0) {
                    requireActivity().resources.getString(R.string.no_duration)
                } else {
                    requireActivity().resources.getQuantityString(
                        R.plurals.duration,
                        it.duration,
                        it.duration
                    )
                }
            binding.cover.setImageURI(it.imageUrl)
            binding.includedPlaylistView.cover.setImageURI(it.imageUrl)
            trackList.clear()
            trackList.addAll(it.trackList)
            playlistAdapter.notifyDataSetChanged()
        }
    }

}

    private fun showDialogDeleteTrack(track:Track) {
        confirmDialog = MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(R.string.delete_track)
            .setMessage("")
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTrack(track)
            }
        confirmDialog.show()
    }

    private fun showDialogDeletePlaylist() {
        val text = requireActivity().getString(R.string.do_you_want_delete_playlist)
        val titleDialog = text + " \"${binding.title.text}\"?"
        confirmDialog = MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(titleDialog)
            .setMessage("")
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deletePlaylist()
            }
        confirmDialog.show()
    }

    private fun sharingPlaylist() {
        val quantityTracks = binding.quantity.text.toString()
        if(quantityTracks == requireActivity().resources.getString(R.string.no_tracks))
            Toast.makeText(
                requireContext(),
                getString(R.string.no_tracks_for_sharing),
                Toast.LENGTH_SHORT
            ).show()
        else {
            viewModel.sharePlaylist(quantityTracks)
        }
    }

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

    companion object {
        private const val ID = "ID"

        fun createArgs(id: Long): Bundle =
            bundleOf(ID to id)
    }

}