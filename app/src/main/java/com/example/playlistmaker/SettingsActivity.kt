package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Toolbar>(R.id.back).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.share).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.link_for_practicum)
            startActivity(shareIntent)
        }

        findViewById<TextView>(R.id.support).setOnClickListener {
            val sendToIntent = Intent(Intent.ACTION_SENDTO)
            sendToIntent.data = Uri.parse("mailto:")
            sendToIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.my_email))
            sendToIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.subject_for_developers)
            sendToIntent.putExtra(Intent.EXTRA_TEXT, R.string.message_for_developers)
            startActivity(sendToIntent)
        }

        findViewById<TextView>(R.id.agreement).setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(agreementIntent)
        }
    }
}