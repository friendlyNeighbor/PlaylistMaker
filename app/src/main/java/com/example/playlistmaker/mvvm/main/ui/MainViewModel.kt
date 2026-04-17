package com.example.playlistmaker.mvvm.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainViewModel(primaryState: MainState) : ViewModel() {

    private val mainLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<MainState> = mainLiveData

    fun goToSearchActivity() {
        mainLiveData.postValue(MainState.SEARCH)
    }
    fun goToMediatekaActivity() {
        mainLiveData.postValue(MainState.MEDIA)
    }
    fun goToSettingsActivity() {
        mainLiveData.postValue(MainState.SETTINGS)
    }

    fun resetState() {
        mainLiveData.postValue(MainState.WAIT)
    }
/*
    companion object {
        fun getFactory(value: MainState): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(value)
            }
        }
    }

 */
}