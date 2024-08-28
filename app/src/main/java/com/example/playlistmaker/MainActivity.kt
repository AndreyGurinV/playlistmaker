package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            Toast.makeText(this@MainActivity, "Нажата кнопка Поиск", Toast.LENGTH_SHORT).show()
        } }
        button.setOnClickListener(imageClickListener)

        findViewById<Button>(R.id.media).setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка Медиатека", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.settings).setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка Настройки", Toast.LENGTH_SHORT).show()
        }
    }

}