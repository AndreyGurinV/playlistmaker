package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository, private var onError: () -> Unit, private var onSuccess: (result: List<Track>) -> Unit) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String){
        executor.execute {
            repository.searchTracks(expression, onError = onError, onSuccess = onSuccess)
        }
    }
}