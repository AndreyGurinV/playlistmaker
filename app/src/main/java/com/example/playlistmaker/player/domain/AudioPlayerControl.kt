package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.data.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun showNotification()
    fun hideNotification()
}