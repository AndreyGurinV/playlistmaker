package com.example.playlistmaker.settings.domain.model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.ThemeInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: ThemeInteractor
): ViewModel() {
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
}