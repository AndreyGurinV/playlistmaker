package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.media.domain.impl.FavoritesInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}