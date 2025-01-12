package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.SearchHistoryIteractor
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryIteractorImpl(private val searchHistoryRepository: SearchHistoryRepository):
    SearchHistoryIteractor {
    override fun load(): Array<Track> {
        return searchHistoryRepository.load()
    }

    override fun addToHistory(track: Track) {
        searchHistoryRepository.addToHistory(track)
    }

    override fun clear() {
        searchHistoryRepository.clear()
    }
}