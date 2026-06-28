package com.example.playlistmaker.mvvm.media.ui.createPlaylist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentCreatePlaylist: Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePlaylistViewModel by viewModel()

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var uriImage: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myColorStateListGrey = ContextCompat.getColorStateList(requireContext(), R.color.selector_text_field)
        val myColorStateListBlack = ContextCompat.getColorStateList(requireContext(), R.color.selector_text_field2)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    uriImage = uri
                    viewModel.pickImage(uri)
                }
            }

        binding.cover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.toolbar.setOnClickListener {
            closeFragment()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closeFragment()
        }


        binding.titlePlaylist.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.titleInputLayout.defaultHintTextColor = myColorStateListBlack
                binding.buttonCreate.isEnabled=false

            } else {
                binding.titleInputLayout.defaultHintTextColor = myColorStateListGrey
                binding.buttonCreate.isEnabled=true
            }
        }

        binding.description.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.descriptionInputLayout.defaultHintTextColor = myColorStateListBlack
            } else {
                binding.descriptionInputLayout.defaultHintTextColor = myColorStateListGrey
            }
        }

        binding.buttonCreate.setOnClickListener {
            val textTitle = binding.titlePlaylist.text.toString()
            val textDescription = binding.description.text.toString()

            viewModel.savePlaylist(textTitle, textDescription)

            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_created, textTitle),
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigateUp()
        }

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            if(it.uri!=null) {
                binding.cover.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.cover.setImageURI(it.uri)
            }
        }
    }


    private fun closeFragment() {
        if (uriImage==null &&
            binding.titlePlaylist.text.toString().isEmpty() &&
            binding.description.text.toString().isEmpty()
        )
            findNavController().navigateUp()

        else {
            showDialog()
        }
    }
    private fun showDialog() {
        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.finish_creating)
            .setMessage(R.string.data_will_be_lost)
            .setNeutralButton(R.string.cancel) { _, _ ->    }
            .setPositiveButton(R.string.complete) { _, _ ->
                findNavController().navigateUp()
            }
        confirmDialog.show()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}