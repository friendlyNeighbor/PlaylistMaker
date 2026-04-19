package com.example.playlistmaker.mvvm.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MediatekaViewModel(primaryState: Boolean) : ViewModel() {

    private val mediatekaLiveData = MutableLiveData(primaryState)
    fun getLiveData(): LiveData<Boolean> = mediatekaLiveData

    fun finishActivity() {
        mediatekaLiveData.postValue(true)
    }
}
