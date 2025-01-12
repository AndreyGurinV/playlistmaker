package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.CurrentTheme
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        with(findViewById<SwitchMaterial>(R.id.themeSwitcher)){
            this.isChecked = viewModel.getCurrentTheme(CurrentTheme.isDarkTheme(context))
            this.setOnCheckedChangeListener { switcher, checked ->
                (applicationContext as App).switchTheme(checked)
                viewModel.saveCurrentTheme(checked)

            }
        }

        findViewById<Toolbar>(R.id.back).setNavigationOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.share).setOnClickListener {
            viewModel.shareApp()
//            val shareIntent = Intent(Intent.ACTION_SEND)
//            shareIntent.type = "text/plain"
//            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_for_practicum))
//            startActivity(shareIntent)
        }

        findViewById<TextView>(R.id.support).setOnClickListener {
            viewModel.openSupport()
//            val sendToIntent = Intent(Intent.ACTION_SENDTO)
//            sendToIntent.data = Uri.parse("mailto:")
//            sendToIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.my_email))
//            sendToIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_for_developers))
//            sendToIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_for_developers))
//            startActivity(sendToIntent)
        }

        findViewById<TextView>(R.id.agreement).setOnClickListener {
            viewModel.openTerms()
//            val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_for_agreement)))
//            startActivity(agreementIntent)
        }
    }
}