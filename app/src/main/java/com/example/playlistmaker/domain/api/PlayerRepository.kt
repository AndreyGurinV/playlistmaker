package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun preparePlayer(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

//    fun playbackControl()

    fun release()
}