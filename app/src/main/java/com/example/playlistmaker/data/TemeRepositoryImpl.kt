package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val themeSwitcher: ThemeSwitcher):ThemeRepository {
    override fun getCurrentTheme(defVal: Boolean): Boolean {
        return themeSwitcher.getCurrentTheme(defVal)
    }

    override fun saveCurrentTheme(isDark: Boolean) {
        themeSwitcher.saveCurrentTheme(isDark)
    }
}