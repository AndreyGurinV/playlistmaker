package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String, onError: () -> Unit, onSuccess: (result: List<Track>) -> Unit) {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == REQUEST_OK) {
            onSuccess(
                (response as TracksResponse).results.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                }
            )
            } else {
            onError.invoke()
        }
    }

    companion object {
        const val REQUEST_OK = 200
    }

}