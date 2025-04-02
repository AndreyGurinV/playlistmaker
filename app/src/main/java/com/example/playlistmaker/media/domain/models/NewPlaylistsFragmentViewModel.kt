package com.example.playlistmaker.media.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<String>()
    fun observeNewPlaylist(): LiveData<String> = stateLiveData

    fun createNewPlaylist(playlistTitle: String, playlistDescription: String, pathToImage: String) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                PlaylistDto(
                    id = "",
                    playlistTitle = playlistTitle,
                    playlistDescription = playlistDescription,
                    pathToImage = pathToImage,
                )
            )
        }
        stateLiveData.postValue(playlistTitle)
    }
}