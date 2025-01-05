package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerIteractor
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerIteractorImpl(private val playerRepository: PlayerRepository): PlayerIteractor {

    private var playerState = STATE_DEFAULT
    lateinit var onPlayerStarted_:  () -> Unit
    lateinit var onPlayerPaused_: () -> Unit
    override fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit,
        onPlayerStarted: () -> Unit,
        onPlayerPaused: () -> Unit
    ) {
        onPlayerStarted_ = onPlayerStarted
        onPlayerPaused_ = onPlayerPaused
        playerRepository.preparePlayer(
            url,
            onPreparedListener = {
                onPreparedListener.invoke()
                playerState = STATE_PREPARED
            },
            onCompletionListener = {
                onCompletionListener.invoke()
                playerState = STATE_PAUSED
            })
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
        playerState = STATE_PLAYING
        onPlayerStarted_.invoke()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
        playerState = STATE_PAUSED
        onPlayerPaused_.invoke()
    }

    override fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun release() {
        playerRepository.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}