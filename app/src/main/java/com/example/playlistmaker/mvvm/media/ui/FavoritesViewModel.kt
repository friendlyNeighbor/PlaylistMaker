package com.example.playlistmaker.mvvm.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor): ViewModel() {
    private val favoritesLiveData = MutableLiveData<List<Track>>()
    fun getLiveData(): LiveData<List<Track>> = favoritesLiveData



    fun setFavoritesLayout() {
        viewModelScope.launch {
            val listOfFavorites=favoritesInteractor.getFavoritesTrackList().first()
                if (listOfFavorites.isEmpty())
                    favoritesLiveData.postValue(emptyList())
                else
                    favoritesLiveData.postValue(listOfFavorites)
        }
    }
}
