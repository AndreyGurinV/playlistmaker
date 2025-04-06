package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

    suspend fun getTracks(): Flow<List<Track>>

    suspend fun addTrackPlaylist(track: Track)

    suspend fun removeTrackPlaylist(track: Track)

}