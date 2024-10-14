package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(val sp: SharedPreferences) {
    val USER_KEY = "PlayListMakerHistory"
    fun load(): Array<Track> {
        val json = sp.getString(USER_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun write(tracks: Array<Track>) {
        val json = Gson().toJson(tracks)
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
    }
}