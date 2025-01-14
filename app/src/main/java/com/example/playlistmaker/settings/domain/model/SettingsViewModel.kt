package com.example.playlistmaker.settings.domain.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val sharingInteractor = Creator.provideSharingInteractor(getApplication<Application>().applicationContext)
    private val settingsInteractor = Creator.provideThemeInteractor(getApplication<Application>().applicationContext)

    fun getCurrentTheme(isDark: Boolean): Boolean =
        settingsInteractor.getCurrentTheme(isDark)

    fun saveCurrentTheme(isDark: Boolean) {
        settingsInteractor.saveCurrentTheme(isDark)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }
}