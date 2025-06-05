package com.example.playlistmaker.media.domain.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _tracksFlow = MutableStateFlow<List<Track>>(emptyList())
    val tracksFlow = _tracksFlow.asStateFlow()

    fun loadFavorites(){
        viewModelScope.launch {
            favoritesInteractor.getTracks().collect{
                _tracksFlow.value = it
            }
        }
    }
}