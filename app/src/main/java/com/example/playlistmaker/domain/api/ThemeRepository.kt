package com.example.playlistmaker.domain.api

interface ThemeRepository {
    fun getCurrentTheme(): Boolean

    fun saveCurrentTheme(isDark: Boolean)
}