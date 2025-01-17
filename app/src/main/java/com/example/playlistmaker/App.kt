package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.player.di.playerDataModule
import com.example.playlistmaker.player.di.playerInteractorModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.dataModule
import com.example.playlistmaker.search.di.interactorModule
import com.example.playlistmaker.search.di.repositoryModule
import com.example.playlistmaker.search.di.viewModelModule
import com.example.playlistmaker.settings.domain.ThemeInteractor
import com.example.playlistmaker.settings.domain.di.settingsDataModule
import com.example.playlistmaker.settings.domain.di.settingsInteractorModule
import com.example.playlistmaker.settings.domain.di.settingsViewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App :Application(){

    private val themeInteractor: ThemeInteractor by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(settingsDataModule, settingsInteractorModule, settingsViewModelModule,
                playerDataModule, playerInteractorModule, playerViewModelModule,
                dataModule, repositoryModule, interactorModule, viewModelModule
            )
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