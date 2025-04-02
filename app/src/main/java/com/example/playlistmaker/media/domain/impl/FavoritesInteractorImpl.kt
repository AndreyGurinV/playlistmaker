package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {
    override suspend fun addTrack(track: Track) {
        favoritesRepository.addTrack(track)
    }

    override suspend fun removeTrack(track: Track) {
        favoritesRepository.removeTrack(track)
    }

    override suspend fun getTracks(): Flow<List<Track>> {
        return favoritesRepository.getTracks()
    }

    override suspend fun addTrackPlaylist(track: Track) {
        favoritesRepository.addTrackPlaylist(track)
    }
}