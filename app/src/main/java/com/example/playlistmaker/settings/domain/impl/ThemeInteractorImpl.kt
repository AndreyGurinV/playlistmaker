package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.ThemeIteractor
import com.example.playlistmaker.settings.data.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository): ThemeIteractor {
    override fun getCurrentTheme(defVal: Boolean): Boolean {
        return themeRepository.getCurrentTheme(defVal)
    }

    override fun saveCurrentTheme(isDark: Boolean) {
        themeRepository.saveCurrentTheme(isDark)
    }
}