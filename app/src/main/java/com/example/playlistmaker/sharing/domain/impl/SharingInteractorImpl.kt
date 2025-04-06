package com.example.playlistmaker.sharing.domain.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.EmailData
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun sharePlaylist(text: String) {
        externalNavigator.shareLink(text)
    }

    private fun getShareAppLink(): String =
        context.getString(R.string.link_for_practicum)


    private fun getSupportEmailData(): EmailData =
        EmailData(
            email = context.getString(R.string.my_email),
            subject = context.getString(R.string.subject_for_developers),
            text = context.getString(R.string.subject_for_developers)
        )

    private fun getTermsLink(): String =
        context.getString(R.string.link_for_agreement)

}