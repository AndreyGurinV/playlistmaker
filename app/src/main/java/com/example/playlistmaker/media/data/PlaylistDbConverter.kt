package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.dto.PlaylistDto

class PlaylistDbConverter {
    fun map(playlistEntity: PlaylistEntity): PlaylistDto {
        return PlaylistDto(
            id = playlistEntity.id.toString(),
            playlistTitle = playlistEntity.playlistTitle,
            playlistDescription = playlistEntity.playlistDescription,
            pathToImage = playlistEntity.pathToImage,
            tracksIds = playlistEntity.tracksIds,
            tracksCount = playlistEntity.tracksCount
        )
    }

    fun map(playlist: PlaylistDto): PlaylistEntity {
        return PlaylistEntity(
            id = if(playlist.id.isNotEmpty()) playlist.id.toInt() else 0,
            playlistTitle = playlist.playlistTitle,
            playlistDescription = playlist.playlistDescription,
            pathToImage = playlist.pathToImage,
            tracksIds = playlist.tracksIds,
            tracksCount = playlist.tracksCount
        )
    }
}