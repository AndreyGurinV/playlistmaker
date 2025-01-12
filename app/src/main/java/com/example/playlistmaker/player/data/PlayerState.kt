package com.example.playlistmaker.player.data

sealed interface PlayerState {
    object Prepared: PlayerState

    object Completion: PlayerState

    object Started: PlayerState

    object Paused: PlayerState
}