package com.example.playlistmaker.mvvm.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MediatekaViewModel(primaryState: Boolean) : ViewModel() {

    private val mediatekaLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<Boolean> = mediatekaLiveData

    fun finishActivity() {
        mediatekaLiveData.postValue(true)
    }
/*
    companion object {
        fun getFactory(value: Boolean): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediatekaViewModel(value)
            }
        }
    }

 */
}