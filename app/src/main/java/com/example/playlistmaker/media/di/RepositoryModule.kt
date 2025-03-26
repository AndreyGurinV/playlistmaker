package com.example.playlistmaker.media.di

import androidx.room.Room
import com.example.playlistmaker.media.data.FavoritesRepositoryImpl
import com.example.playlistmaker.media.data.TrackDbConverter
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.dao.FavoritesDao
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    factory { TrackDbConverter() }

    single<FavoritesDao> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build().favoritesDao()
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
}