package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.MediaActivity
import com.example.playlistmaker.settings.ui.SettingsActivity
import com.example.playlistmaker.search.ui.FindActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<Button>(R.id.find)
        val imageClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            val displayIntent = Intent(this@MainActivity, FindActivity::class.java)
            startActivity(displayIntent)
        }
        }
        button.setOnClickListener(imageClickListener)

        findViewById<Button>(R.id.media).setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }
        findViewById<Button>(R.id.settings).setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }

}