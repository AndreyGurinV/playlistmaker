package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val player: MediaPlayer): PlayerRepository {
    override fun preparePlayer(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            onPreparedListener.invoke()
        }
        player.setOnCompletionListener {
            onCompletionListener.invoke()
        }
    }

    override fun startPlayer() {
        player.start()
    }

    override fun pausePlayer() {
        player.pause()
    }

    override fun release() {
        player.release()
    }
}