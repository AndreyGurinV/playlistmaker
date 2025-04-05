package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.models.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    factory {
        MediaPlayer()
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    viewModel {
        PlayerViewModel(get(), get(), get())
    }

}