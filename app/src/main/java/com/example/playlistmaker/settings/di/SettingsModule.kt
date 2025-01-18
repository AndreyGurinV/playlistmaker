package com.example.playlistmaker.settings.di

import android.content.Context
import com.example.playlistmaker.search.di.PLAY_LIST_PREFERENCES
import com.example.playlistmaker.settings.data.ThemeRepository
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.data.ThemeSwitcher
import com.example.playlistmaker.settings.domain.ThemeInteractor
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.settings.domain.model.SettingsViewModel
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single {
        androidContext()
            .getSharedPreferences(PLAY_LIST_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<ThemeSwitcher> {
        ThemeSwitcher(get())
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigator(androidContext())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), androidContext())
    }

    single<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

}