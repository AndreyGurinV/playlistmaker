package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.TracksRepository
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

}