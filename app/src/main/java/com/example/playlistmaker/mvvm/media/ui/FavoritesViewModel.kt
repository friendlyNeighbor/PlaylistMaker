package com.example.playlistmaker.mvvm.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FavoritesViewModel: ViewModel() {
    private val favoritesLiveData = MutableLiveData<FavoritesState>()
    fun getLiveData(): LiveData<FavoritesState> = favoritesLiveData

    fun favoritesIsEmpty() {
        favoritesLiveData.postValue(FavoritesState.EMPTY)
    }
}