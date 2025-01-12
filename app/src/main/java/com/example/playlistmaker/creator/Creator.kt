package com.example.playlistmaker.creator

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.data.ThemeSwitcher
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.settings.data.ThemeRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.data.TracksRepository
import com.example.playlistmaker.player.domain.impl.PlayerIteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryIteractorImpl
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

const val PLAY_LIST_PREFERENCES = "play_list_preferences"

object Creator {
    private fun getTracksRepository() : TracksRepository =
        TracksRepositoryImpl(RetrofitNetworkClient())

    fun provideTracksInteractor() : TracksInteractor =
        TracksInteractorImpl(getTracksRepository())

    private fun getThemeSwitcher(context: Context) =
        ThemeSwitcher(context.getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE))
    private fun getThemeRepository(context: Context): ThemeRepository =
        ThemeRepositoryImpl(getThemeSwitcher(context))

    fun provideThemeInteractor(context: Context): ThemeInteractorImpl =
        ThemeInteractorImpl(getThemeRepository(context))

    private fun getExternalNavigator(context: Context): ExternalNavigator =
        ExternalNavigator(context)

    fun provideSharingInteractor(context: Context): SharingInteractorImpl =
        SharingInteractorImpl(getExternalNavigator(context), context)

    private fun getSearchHistory(context: Context) =
        SearchHistory(context.getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE))
    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(getSearchHistory(context))

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryIteractorImpl =
        SearchHistoryIteractorImpl(getSearchHistoryRepository(context))

    private fun getPlayer() =
        MediaPlayer()

    private fun getPlayerRepositiry(): PlayerRepositoryImpl =
        PlayerRepositoryImpl(getPlayer())

    fun providePlayerIteractor(): PlayerIteractorImpl =
        PlayerIteractorImpl(getPlayerRepositiry())
}