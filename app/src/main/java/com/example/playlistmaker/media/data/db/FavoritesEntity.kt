package com.example.playlistmaker.media.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class FavoritesEntity(
    @PrimaryKey
    val id: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)
