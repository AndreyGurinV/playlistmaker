package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri

class ExternalNavigator(private val context: Context) {
    fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        context.startActivity(shareIntent)
    }

    fun openLink(link: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        agreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
        context.startActivity(agreementIntent)
    }

    fun openEmail(mail: EmailData) {
        val sendToIntent = Intent(Intent.ACTION_SENDTO)
        sendToIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
        sendToIntent.data = Uri.parse("mailto:")
        sendToIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail.email))
        sendToIntent.putExtra(Intent.EXTRA_SUBJECT, mail.subject)
        sendToIntent.putExtra(Intent.EXTRA_TEXT, mail.text)
        context.startActivity(sendToIntent)
    }

}