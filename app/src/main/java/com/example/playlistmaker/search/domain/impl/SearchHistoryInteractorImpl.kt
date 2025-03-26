package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository,
): SearchHistoryInteractor {
    override suspend fun load() =
        searchHistoryRepository.load()

    override fun addToHistory(track: Track) {
        searchHistoryRepository.addToHistory(track)
    }

    override fun clear() {
        searchHistoryRepository.clear()
    }
}