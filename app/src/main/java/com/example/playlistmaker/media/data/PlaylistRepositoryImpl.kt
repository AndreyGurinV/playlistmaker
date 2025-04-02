package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter
): PlaylistRepository {
    override suspend fun insertPlaylist(playlist: PlaylistDto) {
        playlistDao.insertPlaylist(convertFromPlaylist(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<PlaylistDto>> = flow {
        val playlists = playlistDao.getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlistEntity: List<PlaylistEntity>): List<PlaylistDto> =
        playlistEntity.map { playlist -> playlistDbConverter.map(playlist) }

    private fun convertFromPlaylist(playlist: PlaylistDto): PlaylistEntity =
        playlistDbConverter.map(playlist)

}