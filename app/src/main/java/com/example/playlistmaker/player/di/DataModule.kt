package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import org.koin.dsl.module

val playerDataModule = module {

    factory {
        MediaPlayer()
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }
}