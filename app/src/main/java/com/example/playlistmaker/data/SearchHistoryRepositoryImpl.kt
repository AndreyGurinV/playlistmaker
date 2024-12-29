package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl(private val searchHistory: SearchHistory): SearchHistoryRepository
{
    override fun load(): Array<Track> {
        return searchHistory.load()
    }

    override fun addToHistory(track: Track) {
        searchHistory.addToHistory(track)
    }

    override fun clear() {
        searchHistory.clear()
    }
}