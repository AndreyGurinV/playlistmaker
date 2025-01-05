package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryIteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

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