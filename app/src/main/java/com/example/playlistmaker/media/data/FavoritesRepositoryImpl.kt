package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.FavoritesEntity
import com.example.playlistmaker.media.data.db.dao.FavoritesDao
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val favoritesDao: FavoritesDao,
    private val trackDbConverter: TrackDbConverter
) : FavoritesRepository {
    override suspend fun addTrack(track: Track) {
        favoritesDao.insertTrack(convertFromTrack(track))
    }

    override suspend fun removeTrack(track: Track) {
        favoritesDao.deleteTrack(convertFromTrack(track))
    }

    override suspend fun getTracks(): Flow<List<Track>> = flow {
        val tracks = favoritesDao.getTracks()
        emit(convertFromFavoritesEntity(tracks))
    }

    private fun convertFromFavoritesEntity(favoritesEntity: List<FavoritesEntity>): List<Track> =
        favoritesEntity.map { track -> trackDbConverter.map(track) }

    private fun convertFromTrack(track: Track): FavoritesEntity =
        trackDbConverter.map(track)
}