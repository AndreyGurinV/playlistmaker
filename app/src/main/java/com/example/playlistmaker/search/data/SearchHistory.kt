package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson


class SearchHistory(
    private val sp: SharedPreferences,
    private val gson: Gson
) {
    fun load(): Array<Track> {
        val json = sp.getString(USER_KEY, null) ?: return emptyArray()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    private fun write(tracks: Array<Track>) {
        val json = gson.toJson(tracks)
        sp.edit()
            .putString(USER_KEY, json)
            .apply()
    }
    fun addToHistory(track: Track) {
        var history = mutableListOf<Track>()
        history.addAll(load())
        history.indexOfFirst {
            it.trackId == track.trackId
        }.let {
            if (it != -1)
                history.removeAt(it)
        }
        history.reverse()
        history.add(track)
        history.reverse()
        if (history.size > MAX_HISTORY_SIZE)
            history.removeLast()
        write(history.toTypedArray())
    }

    fun clear() {
        sp.edit()
            .clear()
            .apply()
    }
    companion object {
        const val MAX_HISTORY_SIZE = 10
        const val USER_KEY = "PlayListMakerHistory"
    }
}