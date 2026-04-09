package com.example.playlistmaker.mvvm.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.mvvm.creator.Creator
import com.example.playlistmaker.mvvm.sharing.ExternalNavigator
import com.example.playlistmaker.mvvm.sharing.data.SharingInteractorImpl
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractor

class SettingsViewModel(primaryState: SettingsState) : ViewModel() {

    private val settingsLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<SettingsState> = settingsLiveData

    val externalNavigator = ExternalNavigator()
    val sharingInteractor: SharingInteractor = SharingInteractorImpl(externalNavigator)
    val storageThemeInteractor = Creator.provideStorageInteractor(DARK_THEME)

    fun share() {
        sharingInteractor.shareApp()
    }

    fun support() {
        sharingInteractor.openSupport()
    }

    fun agreement() {
        sharingInteractor.openTerms()
    }

    fun updateTheme() {
        val themeIsDark = storageThemeInteractor.get()
        if(themeIsDark as Boolean)
            settingsLiveData.postValue(SettingsState.NIGHT)
        else
            settingsLiveData.postValue(SettingsState.DAY)
    }

    fun switchTheme(switchOn:Boolean) {

        if(switchOn && settingsLiveData.value== SettingsState.DAY ) {
            storageThemeInteractor.save(switchOn)
            settingsLiveData.postValue(SettingsState.NIGHT)
        }

        if(!switchOn && settingsLiveData.value== SettingsState.NIGHT ) {
            storageThemeInteractor.save(switchOn)
            settingsLiveData.postValue(SettingsState.DAY)
        }
    }

    companion object {
        fun getFactory(value: SettingsState): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(value)
            }
        }
        const val DARK_THEME = "DARK_THEME"
    }
}