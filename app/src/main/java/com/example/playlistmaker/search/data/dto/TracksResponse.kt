package com.example.playlistmaker.search.data.dto

class TracksResponse(val resultsCount: Int,
    val results: List<TrackDto>) : Response()