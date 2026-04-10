package com.example.playlistmaker.mvvm.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.mvvm.creator.Creator
import com.example.playlistmaker.mvvm.sharing.ExternalNavigator
import com.example.playlistmaker.mvvm.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.mvvm.sharing.domain.SharingRepository

class SettingsViewModel(primaryState: SettingsState) : ViewModel() {

    private val settingsLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SettingsState> = settingsLiveData

    val externalNavigator = ExternalNavigator()
    val sharingRepository: SharingRepository = SharingRepositoryImpl(externalNavigator)
    val themeInteractor = Creator.provideThemeInteractor()

    fun updateSwitcher() {
        val darkModeOn = themeInteractor.getTheme()
        if (darkModeOn)
            settingsLiveData.postValue(SettingsState.NIGHT)
        else
            settingsLiveData.postValue(SettingsState.DAY)

    }

    fun share() {
        sharingRepository.shareApp()
    }

    fun support() {
        sharingRepository.openSupport()
    }

    fun agreement() {
        sharingRepository.openTerms()
    }

    fun switchTheme() {
        themeInteractor.switchTheme()
    }

    companion object {
        fun getFactory(value: SettingsState): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(value)
            }
        }
    }
}