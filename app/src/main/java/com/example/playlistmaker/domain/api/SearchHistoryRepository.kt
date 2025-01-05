package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun load(): Array<Track>

    fun addToHistory(track: Track)

    fun clear()
}