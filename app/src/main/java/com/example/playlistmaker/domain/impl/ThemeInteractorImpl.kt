package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ThemeIteractor
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository): ThemeIteractor {
    override fun getCurrentTheme(): Boolean {
        return themeRepository.getCurrentTheme()
    }

    override fun saveCurrentTheme(isDark: Boolean) {
        themeRepository.saveCurrentTheme(isDark)
    }
}