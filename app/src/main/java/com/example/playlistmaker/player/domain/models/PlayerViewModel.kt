package com.example.playlistmaker.player.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.domain.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerIteractor: PlayerInteractor
): ViewModel()  {
    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData
    private var timerJob: Job? = null

    fun preparePlayer(url: String) {
        playerIteractor.preparePlayer(
            url = url,
            onPreparedListener = {
                stateLiveData.postValue(PlayerState.Prepared())
            },
            onCompletionListener = {
                stateLiveData.postValue(PlayerState.Prepared())
            },
            onPlayerStarted = {
                stateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
                startTimer()
            },
            onPlayerPaused = {
                timerJob?.cancel()
                stateLiveData.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
            }
        )
    }

    fun pausePlayer() {
        playerIteractor.pausePlayer()
        stateLiveData.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    fun playbackControl() {
        playerIteractor.playbackControl()
    }

    fun release() {
        playerIteractor.release()
        stateLiveData.value = PlayerState.Default()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerIteractor.isPlaying()) {
                delay(300L)
                stateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
            stateLiveData.postValue(PlayerState.Prepared())
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerIteractor.currentPosition()) ?: "00:00"
    }
}