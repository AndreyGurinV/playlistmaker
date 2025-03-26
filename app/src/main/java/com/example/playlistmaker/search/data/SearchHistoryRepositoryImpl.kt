package com.example.playlistmaker.search.data

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val searchHistory: SearchHistory,
    private val appDatabase: AppDatabase,
): SearchHistoryRepository
{
    override suspend fun load() = flow {
        val tracks = appDatabase.favoritesDao().getTracksIds()
        emit(searchHistory.load().apply {
            for (track in this) {
                track.isFavorite = tracks.find { it_->
                    it_ == track.trackId.toString()
                } != null
            }
        })
    }

    override fun addToHistory(track: Track) {
        searchHistory.addToHistory(track)
    }

    override fun clear() {
        searchHistory.clear()
    }
}