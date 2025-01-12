package com.example.playlistmaker.settings.data

interface ThemeRepository {
    fun getCurrentTheme(defVal: Boolean): Boolean

    fun saveCurrentTheme(isDark: Boolean)
}