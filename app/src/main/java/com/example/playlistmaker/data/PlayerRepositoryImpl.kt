package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val player: Player): PlayerRepository {
    override fun preparePlayer(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        player.preparePlayer(url, onPreparedListener, onCompletionListener)
    }

    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun release() {
        player.release()
    }
}