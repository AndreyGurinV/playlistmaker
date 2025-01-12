package com.example.playlistmaker.settings.data

import android.content.SharedPreferences

class ThemeSwitcher(private val sp: SharedPreferences) {

    fun getCurrentTheme(defVal: Boolean): Boolean {
        return sp.getBoolean(USER_KEY, defVal)
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