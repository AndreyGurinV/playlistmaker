package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun load(): Array<Track>

    fun addToHistory(track: Track)

    fun clear()
}