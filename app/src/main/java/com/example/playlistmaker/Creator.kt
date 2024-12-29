package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.ThemeRepositoryImpl
import com.example.playlistmaker.data.ThemeSwitcher
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.SearchHistoryIteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.Track

object Creator {
    private fun getTracksRepository() : TracksRepository =
        TracksRepositoryImpl(RetrofitNetworkClient())

    fun provideTracksInteractor(onError: () -> Unit, onSuccess: (result: List<Track>) -> Unit) : TracksInteractor =
        TracksInteractorImpl(getTracksRepository(), onError = onError, onSuccess = onSuccess)

    private fun getThemeSwitcher(context: Context) =
        ThemeSwitcher(context)
    private fun getThemeRepository(context: Context): ThemeRepository =
        ThemeRepositoryImpl(getThemeSwitcher(context))

    fun provideThemeInteractor(context: Context): ThemeInteractorImpl =
        ThemeInteractorImpl(getThemeRepository(context))

    private fun getSearchHistory(context: Context) =
        SearchHistory(context)
    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(getSearchHistory(context))

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryIteractorImpl =
        SearchHistoryIteractorImpl(getSearchHistoryRepository(context))
}