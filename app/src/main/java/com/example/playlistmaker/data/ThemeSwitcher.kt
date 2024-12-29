package com.example.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences

class ThemeSwitcher(val context: Context) {
    private val sp: SharedPreferences = context.getSharedPreferences(PLAY_LIST_PREFERENCES,
        Context.MODE_PRIVATE
    )

    fun getCurrentTheme(): Boolean {
        return sp.getBoolean(USER_KEY, false)
    }

    fun saveCurrentTheme(isDark: Boolean){
        sp.edit()
            .putBoolean(USER_KEY, isDark)
            .apply()

    }

    companion object {
        const val USER_KEY = "PlayListMakerTheme"
    }

}