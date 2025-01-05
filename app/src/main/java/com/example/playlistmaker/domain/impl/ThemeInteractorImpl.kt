package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ThemeIteractor
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository): ThemeIteractor {
    override fun getCurrentTheme(defVal: Boolean): Boolean {
        return themeRepository.getCurrentTheme(defVal)
    }

    override fun saveCurrentTheme(isDark: Boolean) {
        themeRepository.saveCurrentTheme(isDark)
    }
}