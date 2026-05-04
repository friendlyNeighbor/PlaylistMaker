package com.example.playlistmaker.mvvm.media.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class FragmentFavorites : Fragment() {
    private val viewModel: FavoritesViewModel by viewModel()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favoritesIsEmpty()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            if (it == FavoritesState.EMPTY)
                binding.favoritesIsEmpty.visibility = VISIBLE
            else
                binding.favoritesIsEmpty.visibility = GONE
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
