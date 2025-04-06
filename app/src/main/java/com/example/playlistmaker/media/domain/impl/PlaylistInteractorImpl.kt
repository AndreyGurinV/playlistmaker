package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.search.domain.models.Track
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

    override suspend fun getPlaylist(id: Int): Flow<PlaylistDto> {
        return playlistRepository.getPlaylist(id)
    }

    override suspend fun getTracks(ids: List<String>): Flow<List<Track>> {
        return playlistRepository.getTracks(ids)
    }

    override suspend fun removePlaylist(playlist: PlaylistDto) {
        playlistRepository.removePlaylist(playlist)
    }
}