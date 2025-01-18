package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track

sealed interface TracksState {
    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

    data class Error(
        val errorMessageId: Int
    ): TracksState

    data class Empty(
        val messageTextId: Int
    ): TracksState
}