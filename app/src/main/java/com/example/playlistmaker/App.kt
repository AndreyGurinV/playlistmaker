package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App :Application(){

    override fun onCreate() {
        super.onCreate()
        switchTheme(
            Creator.provideThemeInteractor(this)
            .getCurrentTheme(CurrentTheme.isDarkTheme(this)))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}