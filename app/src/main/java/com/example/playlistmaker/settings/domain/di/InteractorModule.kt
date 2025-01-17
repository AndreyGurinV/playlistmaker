package com.example.playlistmaker.settings.domain.di

import com.example.playlistmaker.settings.domain.ThemeInteractor
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsInteractorModule = module {

    single<ExternalNavigator> {
        ExternalNavigator(androidContext())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), androidContext())
    }

    single<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }


}