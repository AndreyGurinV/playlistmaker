package com.example.playlistmaker.settings.domain

interface ThemeInteractor {
    fun getCurrentTheme(defVal: Boolean): Boolean

    fun saveCurrentTheme(isDark: Boolean)
}