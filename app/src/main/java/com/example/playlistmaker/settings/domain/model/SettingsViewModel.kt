package com.example.playlistmaker.settings.domain.model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.ThemeInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: ThemeInteractor
): ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()
    init {
        _isDarkTheme.value = getCurrentTheme(false)
    }
    fun getCurrentTheme(isDark: Boolean): Boolean =
        settingsInteractor.getCurrentTheme(isDark)

    fun saveCurrentTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
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