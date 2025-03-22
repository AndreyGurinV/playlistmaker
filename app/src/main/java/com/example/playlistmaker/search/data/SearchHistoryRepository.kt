package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun load(): Flow<Array<Track>>

    fun addToHistory(track: Track)

    fun clear()
}