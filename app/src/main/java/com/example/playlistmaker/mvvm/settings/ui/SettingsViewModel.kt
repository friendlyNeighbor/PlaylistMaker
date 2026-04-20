package com.example.playlistmaker.mvvm.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mvvm.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractor

class SettingsViewModel(primaryState: SettingsState, private val sharingInteractor: SharingInteractor, private val themeInteractor: ThemeInteractor) : ViewModel() {

    private val settingsLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SettingsState> = settingsLiveData

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
}
