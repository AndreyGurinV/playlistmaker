package com.example.playlistmaker.settings.data

class ThemeRepositoryImpl(private val themeSwitcher: ThemeSwitcher): ThemeRepository {
    override fun getCurrentTheme(defVal: Boolean): Boolean {
        return themeSwitcher.getCurrentTheme(defVal)
    }

    override fun saveCurrentTheme(isDark: Boolean) {
        themeSwitcher.saveCurrentTheme(isDark)
    }
}