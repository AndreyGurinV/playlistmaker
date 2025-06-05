package com.example.playlistmaker.media.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
//    private val stateLiveData = MutableLiveData<List<PlaylistDto>>()
//    fun observeState(): LiveData<List<PlaylistDto>> = stateLiveData

    private val _playlistsFlow = MutableStateFlow<List<PlaylistDto>>(emptyList())
    val playlistsFlow = _playlistsFlow.asStateFlow()

    fun load(){
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect{
                _playlistsFlow.value = it
//                stateLiveData.postValue(it)
            }
        }
    }
}