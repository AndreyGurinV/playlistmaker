package com.example.playlistmaker.player.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.domain.PlayerInteractor

class PlayerViewModel(
    private val playerIteractor: PlayerInteractor
): ViewModel()  {
    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun preparePlayer(url: String) {
        playerIteractor.preparePlayer(
            url = url,
            onPreparedListener = {
                renderState(PlayerState.Prepared)
            },
            onCompletionListener = {
                renderState(PlayerState.Completion)
            },
            onPlayerStarted = {
                renderState(PlayerState.Started)
            },
            onPlayerPaused = {
                renderState(PlayerState.Paused)
            }
        )
    }

    fun pausePlayer() {
        playerIteractor.pausePlayer()
    }

    fun playbackControl() {
        playerIteractor.playbackControl()
    }

    fun release() {
        playerIteractor.release()
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }
}