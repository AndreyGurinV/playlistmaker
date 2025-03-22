package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    suspend fun load(): Flow<Array<Track>>

    fun addToHistory(track: Track)

    fun clear()
}