package com.example.playlistmaker.media.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val playlistTitle: String,
    val playlistDescription: String,
    val pathToImage: String,
    val tracksIds: String,
    val tracksCount: String
)
