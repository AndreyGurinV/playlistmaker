package com.example.playlistmaker.domain.api

interface ThemeRepository {
    fun getCurrentTheme(defVal: Boolean): Boolean

    fun saveCurrentTheme(isDark: Boolean)
}