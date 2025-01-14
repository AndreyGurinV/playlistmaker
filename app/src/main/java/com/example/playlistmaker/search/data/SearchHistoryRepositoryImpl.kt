package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track

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