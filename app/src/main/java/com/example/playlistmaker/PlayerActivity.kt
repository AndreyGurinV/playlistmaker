package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity() : AppCompatActivity() {

    private lateinit var imageAlbumCover: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvAlbumName: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvPrimaryGenreName: TextView
    private lateinit var tvCountry: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageAlbumCover = findViewById(R.id.ivAlbumCover)
        tvTrackName = findViewById(R.id.tvTrackName)
        tvArtistName = findViewById(R.id.tvArtistName)
        tvDuration = findViewById(R.id.tvProgress)
        tvAlbumName = findViewById(R.id.tvAlbum)
        tvReleaseDate = findViewById(R.id.tvReleaseDate)
        tvPrimaryGenreName = findViewById(R.id.tvPrimaryGenreName)
        tvCountry = findViewById(R.id.tvCountry)

        findViewById<Toolbar>(R.id.tbBackFromPlayer).setNavigationOnClickListener {
            finish()
        }
        setCurrentTrack(track = intent.extras?.getSerializable("track") as Track)
    }

    fun setCurrentTrack(track: Track) {
        Glide.with(imageAlbumCover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.default_album_icon)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(imageAlbumCover)
        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvDuration.text = track.getDuration()
        tvAlbumName.text = track.collectionName
        tvReleaseDate.text = track.getReleaseYear()
        tvPrimaryGenreName.text = track.primaryGenreName
        tvCountry.text = track.country
    }
}