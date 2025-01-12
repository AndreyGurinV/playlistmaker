package com.example.playlistmaker.settings.domain

interface ThemeIteractor {
    fun getCurrentTheme(defVal: Boolean): Boolean

    fun saveCurrentTheme(isDark: Boolean)
}