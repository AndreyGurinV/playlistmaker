package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App :Application(){
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        switchTheme(getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE)
            .getBoolean(USER_KEY, false)
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE)
            .edit()
            .putBoolean(USER_KEY, darkThemeEnabled)
            .apply()
    }

    companion object {
        const val USER_KEY = "PlayListMakerTheme"
    }
}