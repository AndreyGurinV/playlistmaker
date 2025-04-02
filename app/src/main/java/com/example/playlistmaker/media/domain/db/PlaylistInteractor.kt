package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.dto.PlaylistDto
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertPlaylist(playlist: PlaylistDto)

    suspend fun getPlaylists(): Flow<List<PlaylistDto>>
}