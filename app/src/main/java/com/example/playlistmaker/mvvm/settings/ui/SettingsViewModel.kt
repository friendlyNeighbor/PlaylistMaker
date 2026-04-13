package com.example.playlistmaker.mvvm.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.mvvm.creator.Creator

class SettingsViewModel(primaryState: SettingsState) : ViewModel() {

    private val settingsLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SettingsState> = settingsLiveData

    val sharingInteractor = Creator.provideSharingInteractor()
    val themeInteractor = Creator.provideThemeInteractor()

    fun updateSwitcher() {
        val darkModeOn = themeInteractor.getTheme()
        if (darkModeOn)
            settingsLiveData.postValue(SettingsState.NIGHT)
        else
            settingsLiveData.postValue(SettingsState.DAY)

    }

    fun share() {
        sharingInteractor.shareApp()
    }

    fun support() {
        sharingInteractor.openSupport()
    }

    fun agreement() {
        sharingInteractor.openTerms()
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