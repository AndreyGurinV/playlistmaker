package com.example.playlistmaker.domain.api

interface ThemeIteractor {
    fun getCurrentTheme(defVal: Boolean): Boolean

    fun saveCurrentTheme(isDark: Boolean)
}