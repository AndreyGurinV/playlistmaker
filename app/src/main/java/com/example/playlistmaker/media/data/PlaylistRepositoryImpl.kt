package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.TracksEntity
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.TracksDao
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val tracksDao: TracksDao,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter
): PlaylistRepository {
    override suspend fun insertPlaylist(playlist: PlaylistDto) {
        playlistDao.insertPlaylist(convertFromPlaylist(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<PlaylistDto>> = flow {
        val playlists = playlistDao.getPlaylists()
        emit(convertFromPlaylistsEntity(playlists))
    }

    override suspend fun getPlaylist(id: Int): Flow<PlaylistDto> = flow {
        val playlist = playlistDao.getPlaylist(id)
        emit(playlistDbConverter.map(playlist))
    }

    override suspend fun getTracks(ids: List<String>): Flow<List<Track>> = flow {
        val tracks = tracksDao.getTracks()
        val orderedTracks: MutableList<TracksEntity> = mutableListOf()
        ids.reversed().forEach { id ->
            tracks.find { it.id == id }?.let {
                orderedTracks.add(it)
            }
        }
        emit(convertFromTracksEntity(orderedTracks))
    }

    override suspend fun removePlaylist(playlist: PlaylistDto) {
        playlistDao.deletePlaylist(convertFromPlaylist(playlist))
        playlist.tracksIds.split(";").forEach {it ->
            val playlists = playlistDao.getPlaylists()
            var forDel = true
            playlists.forEach {
                if (it.tracksIds.split(";").contains(it.toString()))
                    forDel = false
            }
            if (forDel)
                tracksDao.removeTrack(it.toInt())
        }
    }

    private fun convertFromPlaylistsEntity(playlistEntity: List<PlaylistEntity>): List<PlaylistDto> =
        playlistEntity.map { playlist -> playlistDbConverter.map(playlist) }

    private fun convertFromPlaylist(playlist: PlaylistDto): PlaylistEntity =
        playlistDbConverter.map(playlist)

    private fun convertFromTracksEntity(tracksEntity: List<TracksEntity>): List<Track> =
        tracksEntity.map { it -> trackDbConverter.mapFromTracksEntity(it) }
}