package com.example.playlistmaker.domain.api

interface PlayerIteractor {

    fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
        onPlayerStarted: () -> Unit,
        onPlayerPaused: () -> Unit
    )

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl()

    fun release()
}