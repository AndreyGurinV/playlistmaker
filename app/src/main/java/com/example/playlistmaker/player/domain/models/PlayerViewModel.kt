package com.example.playlistmaker.player.domain.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.data.PlayerState

class PlayerViewModel(application: Application): AndroidViewModel(application)  {

    private val playerIteractor = Creator.providePlayerIteractor()
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }
}