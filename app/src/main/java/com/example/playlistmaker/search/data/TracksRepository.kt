package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}