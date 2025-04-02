package com.example.playlistmaker.media.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<List<PlaylistDto>>()
    fun observeState(): LiveData<List<PlaylistDto>> = stateLiveData

    fun load(){
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect{
                stateLiveData.postValue(it)
            }
        }
    }
}