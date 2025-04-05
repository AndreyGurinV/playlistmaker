package com.example.playlistmaker.media.domain.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.data.dto.PlaylistInfoDto
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val sharingInteractor: SharingInteractor,

    ) : ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistInfoDto>()
    fun observeState(): LiveData<PlaylistInfoDto> = stateLiveData

    private lateinit var playlistDto: PlaylistDto
    private lateinit var currentTracks: List<Track>

    fun loadPlaylist(id: String){
        viewModelScope.launch {
            id.toIntOrNull()?.let { id ->
                playlistInteractor.getPlaylist(id).collect{ playlist ->
                    playlistDto = playlist
                    with(playlist.tracksIds.split(";")){
                        if (this.isNotEmpty())
                            playlistInteractor.getTracks(this).collect{ tracks ->
                                currentTracks = tracks
                                stateLiveData.postValue(
                                    PlaylistInfoDto(
                                        playlist = playlist,
                                        tracks = tracks,
                                        tracksTime = calcTotalTime(tracks)
                                    )
                                )
                            }
                    }
                }
            }
        }
    }

    fun removeTrack(track: Track) {
        currentTracks = currentTracks.filter { it.trackId != track.trackId }

        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                PlaylistDto(
                    id = playlistDto.id,
                    playlistTitle = playlistDto.playlistTitle,
                    playlistDescription = playlistDto.playlistDescription,
                    pathToImage = playlistDto.pathToImage,
                    tracksIds = currentTracks.joinToString(";"),
                    tracksCount = currentTracks.size.toString()
                ).apply {
                    playlistDto = this
                }
            )
        }.invokeOnCompletion {
            viewModelScope.launch {
                favoritesInteractor.removeTrackPlaylist(track)
            }.invokeOnCompletion {
                stateLiveData.postValue(
                    PlaylistInfoDto(
                        playlist = playlistDto,
                        tracks = currentTracks,
                        tracksTime = calcTotalTime(currentTracks)
                    )
                )

            }
        }
    }

    fun removePlaylist() {
        viewModelScope.launch {
            playlistInteractor.removePlaylist(playlistDto)
        }
    }

    fun sharePlaylist() {
        sharingInteractor.sharePlaylist(
            "${playlistDto.playlistTitle}\n" +
                "${currentTracks.size} треков\n" +
                currentTracks.mapIndexed { index, track ->
                    "${index + 1}. ${track.artistName} - ${track.trackName} ${track.getDuration()}"
                }.joinToString("\n")
        )
    }

    private fun calcTotalTime(tracks: List<Track>): String {
        var sum = 0
        for (track in tracks){
            sum += track.trackTimeMillis
        }
        return SimpleDateFormat("mm", Locale.getDefault()).format(sum)?:"0"
    }
}