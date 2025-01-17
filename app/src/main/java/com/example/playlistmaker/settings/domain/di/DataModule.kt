package com.example.playlistmaker.settings.domain.di

import android.content.Context
import com.example.playlistmaker.search.di.PLAY_LIST_PREFERENCES_
import com.example.playlistmaker.settings.data.ThemeRepository
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.data.ThemeSwitcher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDataModule = module {
    single {
        androidContext()
            .getSharedPreferences(PLAY_LIST_PREFERENCES_, Context.MODE_PRIVATE)
    }

    single<ThemeSwitcher> {
        ThemeSwitcher(get())
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }
}