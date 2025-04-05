package com.example.playlistmaker.media.data.dto

import com.example.playlistmaker.search.domain.models.Track

data class PlaylistInfoDto(
    val playlist: PlaylistDto,
    val tracks: List<Track>,
    val tracksTime: String
)
