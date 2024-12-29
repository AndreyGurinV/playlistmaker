package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

const val PLAY_LIST_PREFERENCES = "play_list_preferences"

class SearchHistory(val context: Context) {
    private val sp: SharedPreferences = context.getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE)
    val USER_KEY = "PlayListMakerHistory"
    fun load(): Array<Track> {
        val json = sp.getString(USER_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun write(tracks: Array<Track>) {
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