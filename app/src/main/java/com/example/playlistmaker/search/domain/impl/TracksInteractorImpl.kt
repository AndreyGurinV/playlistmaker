package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.data.TracksRepository
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val res = repository.searchTracks(expression)) {
                is Resource.Success -> {consumer.consume(res.data, null)}
                is Resource.Error -> {consumer.consume(null, res.message)}
            }
        }
    }
}