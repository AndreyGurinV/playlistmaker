package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.models.TracksSearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TracksSearchViewModel(androidContext(), get(), get())
    }

}