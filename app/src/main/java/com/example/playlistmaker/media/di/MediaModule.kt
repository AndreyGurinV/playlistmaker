package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.domain.models.FavoritesFragmentViewModel
import com.example.playlistmaker.media.domain.models.NewPlaylistsFragmentViewModel
import com.example.playlistmaker.media.domain.models.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel {
        FavoritesFragmentViewModel(get())
    }

    viewModel {
        PlaylistsFragmentViewModel(get())
    }

    viewModel {
        NewPlaylistsFragmentViewModel(get())
    }
}