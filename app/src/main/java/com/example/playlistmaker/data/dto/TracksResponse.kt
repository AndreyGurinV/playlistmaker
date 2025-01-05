package com.example.playlistmaker.data.dto

class TracksResponse(val resultsCount: Int,
    val results: List<TrackDto>) : Response()