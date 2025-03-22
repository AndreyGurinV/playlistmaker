package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.FavoritesRepositoryImpl
import com.example.playlistmaker.media.data.TrackDbConverter
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { TrackDbConverter() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
}