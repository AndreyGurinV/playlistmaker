package com.example.playlistmaker.search.data

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow{
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            NO_INTERNET -> emit(Resource.Error("Check connection to internet"))
            REQUEST_OK -> {
                val tracks = appDatabase.favoritesDao().getTracksIds()
                emit(
                    Resource.Success(
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
                                previewUrl = it.previewUrl,
                                isFavorite = tracks.find {it_->
                                    it_ == it.trackId.toString()
                                } != null
                            )
                        }
                    )
                )
            }
            else -> emit(Resource.Error("Server Error"))
        }
    }

    companion object {
        const val REQUEST_OK = 200
        const val NO_INTERNET = -1
    }

}