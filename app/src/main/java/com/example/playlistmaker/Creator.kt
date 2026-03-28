package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.domain.api.Storage
import com.example.playlistmaker.data.StorageSharedPrefImpl
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.api.TrackRepository

object Creator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    fun provideStorageInteractor(context: Context, key:String): Storage {
        return StorageSharedPrefImpl(context, key)
    }
}