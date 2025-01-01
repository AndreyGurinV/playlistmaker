package com.example.playlistmaker.data

import android.media.MediaPlayer

class Player {

    private var mediaPlayer = MediaPlayer()

    fun preparePlayer(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPreparedListener.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletionListener.invoke()
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
    }

    fun release() {
        mediaPlayer.release()
    }
}