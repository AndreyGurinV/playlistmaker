package com.example.playlistmaker.search.data

import androidx.annotation.StringRes
import com.example.playlistmaker.search.domain.models.Track

sealed interface TracksState {
    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

    data class Error(
        @StringRes val errorMessageId: Int
    ): TracksState

    data class Empty(
        @StringRes val messageTextId: Int
    ): TracksState
}