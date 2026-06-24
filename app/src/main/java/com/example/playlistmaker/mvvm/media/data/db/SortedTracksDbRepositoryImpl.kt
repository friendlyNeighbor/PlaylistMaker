package com.example.playlistmaker.mvvm.media.data.db


import com.example.playlistmaker.mvvm.media.data.db.converters.SortedTracksDbConvertor
import com.example.playlistmaker.mvvm.media.data.db.entity.SortedTrackEntity
import com.example.playlistmaker.mvvm.media.domain.db.TracksRepository
import com.example.playlistmaker.mvvm.search.domain.model.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SortedTracksDbRepositoryImpl (
    private val sortedTracksDbConvertor: SortedTracksDbConvertor,
    private val appDatabase: AppDatabase
): TracksRepository {
    override fun getTrackList(): Flow<List<Track>> = flow{
        val sortedList = appDatabase.getSortedTrackDao().getSortedList()
        emit(convertFromTrackEntityList(sortedList))
    }

    private fun convertFromTrackEntityList(sortedList: List<SortedTrackEntity>): List<Track> {
        return sortedList.map { track -> sortedTracksDbConvertor.map(track) }
    }

    override fun addTrack(track: Track) {
        GlobalScope.launch {   appDatabase.getSortedTrackDao().insertTrack(convertToTrackEntity(track)) }
    }

    override fun deleteTrackById(id: Long) {
        GlobalScope.launch {   appDatabase.getSortedTrackDao().deleteTrackById(id) }
    }

    override fun getIdList(): Flow<List<Long>> = flow {
        val idList = appDatabase.getSortedTrackDao().getIdListOfSorted()
        emit(idList)
    }

    private fun convertToTrackEntity(track: Track): SortedTrackEntity {
        return sortedTracksDbConvertor.map(track) }

}