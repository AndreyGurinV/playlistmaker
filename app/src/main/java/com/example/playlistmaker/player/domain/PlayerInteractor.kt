package com.example.playlistmaker.player.domain

interface PlayerInteractor {

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

    fun isPlaying(): Boolean

    fun currentPosition(): Int

}