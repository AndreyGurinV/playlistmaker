package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val PLAY_LIST_PREFERENCES = "play_list_preferences"

val searchHistoryModule = module {
    single {
        androidContext()
            .getSharedPreferences(PLAY_LIST_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<SearchHistory> {
        SearchHistory(get(), get())
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

}