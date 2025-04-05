package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.media.domain.impl.PlaylistInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}