package com.example.playlistmaker.domain.api

interface ThemeIteractor {
    fun getCurrentTheme(): Boolean

    fun saveCurrentTheme(isDark: Boolean)
}