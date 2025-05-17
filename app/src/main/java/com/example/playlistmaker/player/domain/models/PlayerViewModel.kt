package com.example.playlistmaker.player.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.domain.AudioPlayerControl
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
): ViewModel()  {
    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val favoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavorite(): LiveData<Boolean> = favoriteLiveData

    private val playlistsLiveData = MutableLiveData<List<PlaylistDto>>()
    fun observePlaylists(): LiveData<List<PlaylistDto>> = playlistsLiveData

    private val messageLiveData = MutableLiveData<String>()
    fun observeMessages(): LiveData<String> = messageLiveData


    private lateinit var currentTrack: Track
    private var playlists :ArrayList<PlaylistDto> = arrayListOf()

    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                stateLiveData.postValue(it)
            }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun preparePlayer(track: Track) {
        currentTrack = track
        viewModelScope.launch {
            favoritesInteractor.getTracks().collect{ tracks ->
                favoriteLiveData.postValue(tracks.find { it.trackId == currentTrack.trackId } != null)
            }
        }
    }

    fun playbackControl() {
        audioPlayerControl?.playbackControl()
    }

    fun showNotification() {
        viewModelScope.launch {
            delay(500)
            audioPlayerControl?.showNotification()
        }
    }

    fun hideNotification() {
        audioPlayerControl?.hideNotification()
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

    fun load(){
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect{
                playlists.clear()
                playlists.addAll(it)

                playlistsLiveData.postValue(it)
            }
        }
    }

    fun addTackToPlaylist(playlist: PlaylistDto){
        for (i in playlists){
            if (playlist.playlistTitle == i.playlistTitle) {
                i.tracksIds.split(";").forEach {
                    if (it == currentTrack.trackId.toString()){
                        messageLiveData.postValue("Трек уже добавлен в плейлист ${playlist.playlistTitle}")
                        return
                    }
                }
            }
        }
        viewModelScope.launch {
            favoritesInteractor.addTrackPlaylist(currentTrack)
            playlistInteractor.insertPlaylist(
                PlaylistDto(
                    id = playlist.id,
                    playlistTitle = playlist.playlistTitle,
                    playlistDescription = playlist.playlistDescription,
                    pathToImage = playlist.pathToImage,
                    tracksIds = playlist.tracksIds +
                            if ((playlist.tracksCount.toIntOrNull()?:0) == 0)
                                "${currentTrack.trackId}"
                            else
                                ";${currentTrack.trackId}",
                    tracksCount = ((playlist.tracksCount.toIntOrNull()?:0) + 1).toString()
                )
            )
        }.invokeOnCompletion {
            messageLiveData.postValue("Добавлено в плейлист ${playlist.playlistTitle}")
        }
    }
}