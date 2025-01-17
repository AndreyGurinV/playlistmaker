package com.example.playlistmaker.settings.domain.di

import com.example.playlistmaker.settings.domain.model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModelModule = module {

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }
}