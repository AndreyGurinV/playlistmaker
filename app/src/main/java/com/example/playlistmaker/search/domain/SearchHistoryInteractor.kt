package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun load(): Array<Track>

    fun addToHistory(track: Track)

    fun clear()
}