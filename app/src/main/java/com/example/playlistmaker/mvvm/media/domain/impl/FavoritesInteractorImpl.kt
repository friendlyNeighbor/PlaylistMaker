package com.example.playlistmaker.mvvm.media.domain.impl

import com.example.playlistmaker.mvvm.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.mvvm.media.domain.db.FavoritesRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {
    override fun getFavoritesTrackList(): Flow<List<Track>> {
        return favoritesRepository.getFavoritesTrackList()
    }

    override fun addTrackInFavorites(track: Track) {
        favoritesRepository.addTrackInFavorites(track)
    }

    override fun deleteTrackFromFavoritesById(id: Long) {
        favoritesRepository.deleteTrackFromFavoritesById(id)
    }

    override fun getFavoritesIdList(): Flow<List<Long>> {
        return favoritesRepository.getFavoritesIdList()
    }
}