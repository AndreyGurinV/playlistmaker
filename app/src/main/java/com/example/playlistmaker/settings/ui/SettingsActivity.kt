package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.CurrentTheme
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        }

        findViewById<TextView>(R.id.support).setOnClickListener {
            viewModel.openSupport()
        }

        findViewById<TextView>(R.id.agreement).setOnClickListener {
            viewModel.openTerms()
        }
    }
}