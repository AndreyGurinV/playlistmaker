package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.media.di.mediaModule
import com.example.playlistmaker.player.di.playerModule
import com.example.playlistmaker.search.di.searchHistoryModule
import com.example.playlistmaker.search.di.searchModule
import com.example.playlistmaker.settings.di.settingsModule
import com.example.playlistmaker.settings.domain.ThemeInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App :Application(){

    private val themeInteractor: ThemeInteractor by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(settingsModule, playerModule, searchHistoryModule, searchModule, mediaModule)
        }
        switchTheme(
            themeInteractor
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