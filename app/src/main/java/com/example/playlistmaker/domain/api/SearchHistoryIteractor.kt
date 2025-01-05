package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryIteractor {
    fun load(): Array<Track>

    fun addToHistory(track: Track)

    fun clear()
}