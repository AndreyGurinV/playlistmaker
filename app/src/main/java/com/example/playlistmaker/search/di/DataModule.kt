package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.network.ItunesAPI
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PLAY_LIST_PREFERENCES_ = "play_list_preferences"

val dataModule = module {

    single<ItunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesAPI::class.java)
    }

        single {
            androidContext()
                .getSharedPreferences(PLAY_LIST_PREFERENCES_, Context.MODE_PRIVATE)
        }

    factory { Gson() }

    single<SearchHistory> {
        SearchHistory(get(), get())
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get() )
    }

}