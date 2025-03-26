package com.example.playlistmaker.media.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<List<Track>>()
    fun observeState(): LiveData<List<Track>> = stateLiveData

    fun loadFavorites(){
        viewModelScope.launch {
            favoritesInteractor.getTracks().collect{
                stateLiveData.postValue(it)
            }
        }
    }
}