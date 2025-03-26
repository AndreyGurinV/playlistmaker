package com.example.playlistmaker.search.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.TracksState
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val searchHistory: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
): ViewModel()  {
    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText)
                    .collect{
                        val tracks = mutableListOf<Track>()
                        if (it.first != null) {
                            tracks.addAll(it.first!!)
                        }

                        when {
                            it.second != null -> {
                                renderState(
                                    TracksState.Error(
                                        errorMessageId = R.string.something_went_wrong,
                                    )
                                )
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    TracksState.Empty(
                                        messageTextId = R.string.nothing_found,
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    TracksState.Content(
                                        tracks = tracks,
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun load() {
        viewModelScope.launch {
            searchHistory.load().collect{
                renderState(
                    TracksState.History(
                        tracks = it.asList()
                    )
                )
            }
        }
    }

    fun addToHistory(track: Track) {
        searchHistory.addToHistory(track)
    }

    fun clear() {
        searchHistory.clear()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}