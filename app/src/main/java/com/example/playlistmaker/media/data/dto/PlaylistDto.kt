package com.example.playlistmaker.media.data.dto

data class PlaylistDto(
    val id: String,
    val playlistTitle: String,
    val playlistDescription: String,
    val pathToImage: String,
    val tracksIds: String = "",
    val tracksCount: String = "0"
)
