package com.example.playlistmaker.player.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerIteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
): ViewModel()  {
    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val favoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavorite(): LiveData<Boolean> = favoriteLiveData

    private var timerJob: Job? = null
    private lateinit var currentTrack: Track

    fun preparePlayer(track: Track) {
        currentTrack = track
        viewModelScope.launch {
            favoritesInteractor.getTracks().collect{ tracks ->
                favoriteLiveData.postValue(tracks.find { it.trackId == currentTrack.trackId } != null)
            }
        }
        playerIteractor.preparePlayer(
            url = track.previewUrl,
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

    fun onFavoriteClicked() {
        if (currentTrack.isFavorite) {
            viewModelScope.launch {
                favoritesInteractor.removeTrack(currentTrack)
            }
            favoriteLiveData.postValue(false)
        } else {
            viewModelScope.launch {
                favoritesInteractor.addTrack(currentTrack)
            }
            favoriteLiveData.postValue(true)
        }
        currentTrack.isFavorite = !currentTrack.isFavorite
    }
}