package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.FavoritesEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(track: Track): FavoritesEntity {
        return FavoritesEntity(
            track.trackId.toString(),
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(favoritesEntity: FavoritesEntity): Track {
        return Track(
            favoritesEntity.id.toInt(),
            favoritesEntity.trackName,
            favoritesEntity.artistName,
            favoritesEntity.trackTimeMillis,
            favoritesEntity.artworkUrl100,
            favoritesEntity.collectionName,
            favoritesEntity.releaseDate,
            favoritesEntity.primaryGenreName,
            favoritesEntity.country,
            favoritesEntity.previewUrl,
            true
        )
    }
}