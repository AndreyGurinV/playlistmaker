package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryIteractor {
    fun load(): Array<Track>

    fun addToHistory(track: Track)

    fun clear()
}