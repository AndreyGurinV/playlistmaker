package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val themeSwitcher: ThemeSwitcher):ThemeRepository {
    override fun getCurrentTheme(): Boolean {
        return themeSwitcher.getCurrentTheme()
    }

    override fun saveCurrentTheme(isDark: Boolean) {
        themeSwitcher.saveCurrentTheme(isDark)
    }
}