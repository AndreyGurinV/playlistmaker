package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: PlaylistDto)

    suspend fun getPlaylists(): Flow<List<PlaylistDto>>

    suspend fun getPlaylist(id: Int): Flow<PlaylistDto>

    suspend fun getTracks(ids: List<String>): Flow<List<Track>>

    suspend fun removePlaylist(playlist: PlaylistDto)
}
