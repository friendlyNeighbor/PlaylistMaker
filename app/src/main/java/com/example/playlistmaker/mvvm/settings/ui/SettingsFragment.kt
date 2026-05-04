package com.example.playlistmaker.mvvm.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val primaryState = SettingsState.DEFAULT
    private val viewModel: SettingsViewModel by viewModel() {
        parametersOf(primaryState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            share.setOnClickListener {
                viewModel.share()
            }
            support.setOnClickListener {
                viewModel.support()
            }
            agreement.setOnClickListener {
                viewModel.agreement()
            }
            switchDarkTheme.setOnClickListener {
                viewModel.switchTheme()
            }
        }

        viewModel.updateSwitcher()

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            if (it == SettingsState.NIGHT && !binding.switchDarkTheme.isChecked) {
                binding.switchDarkTheme.isChecked=true
            }
            else {
                binding.switchDarkTheme.isChecked=false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
