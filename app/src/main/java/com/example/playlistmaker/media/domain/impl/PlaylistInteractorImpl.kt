package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: PlaylistDto) {
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<PlaylistDto>> {
        return playlistRepository.getPlaylists()
    }
}