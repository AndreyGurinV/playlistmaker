package com.example.playlistmaker.search.domain.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.TracksState
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val searchHistory: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
): ViewModel()  {
    private var searchJob: Job? = null

    private val _stateFlow = MutableStateFlow<TracksState>(TracksState.Content(tracks = emptyList()))
    val stateFlow = _stateFlow.asStateFlow()

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()

    private var latestSearchText: String? = null
    val tracks = mutableListOf<Track>()

    fun repeatSearch(changedText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun searchDebounce(changedText: String) {
        _text.value = changedText
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
                        if (it.first != null) {
                            tracks.clear()
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
        _stateFlow.value = state
    }

    fun load() {
        if (latestSearchText?.isNotEmpty() == true){
            renderState(
                TracksState.Content(
                    tracks = tracks,
                )
            )
        } else {
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
    }

    fun addToHistory(track: Track) {
        searchHistory.addToHistory(track)
    }

    fun clear() {
        searchHistory.clear()
        renderState(
            TracksState.History(
                tracks = emptyList()
            )
        )
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}