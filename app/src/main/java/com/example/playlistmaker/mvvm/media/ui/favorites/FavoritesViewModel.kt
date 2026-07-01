package com.example.playlistmaker.mvvm.media.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.db.TracksInteractor
import com.example.playlistmaker.mvvm.player.domain.TrackSaverInteractor
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesTracksInteractor: TracksInteractor, private val trackSaverInteractor: TrackSaverInteractor): ViewModel() {
    private val favoritesLiveData = MutableLiveData<List<Track>>()
    fun getLiveData(): LiveData<List<Track>> = favoritesLiveData

    fun setFavoritesLayout() {
        viewModelScope.launch {
            val listOfFavorites=favoritesTracksInteractor.getTrackList().first()
                if (listOfFavorites.isEmpty())
                    favoritesLiveData.postValue(emptyList())
                else
                    favoritesLiveData.postValue(listOfFavorites)
        }
    }

    fun addTrackInMemory(track: Track) {
        trackSaverInteractor.addTrackInMemory(track)
    }
}