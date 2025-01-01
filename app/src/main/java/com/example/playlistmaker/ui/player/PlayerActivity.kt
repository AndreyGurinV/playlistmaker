package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class PlayerActivity() : AppCompatActivity() {

    private lateinit var imageAlbumCover: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvAlbumName: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvPrimaryGenreName: TextView
    private lateinit var tvCountry: TextView
    private lateinit var btnPlay: ImageButton
    private lateinit var tvCurrentTime: TextView

    private val mediaPlayer = Creator.providePlayerIteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val playRunnable = createUpdateTimerTask()

    var currentTimeSecs = 0
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

        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        btnPlay = findViewById(R.id.btnPlay)
        btnPlay.setOnClickListener {
            playbackControl()
        }

        findViewById<Toolbar>(R.id.tbBackFromPlayer).setNavigationOnClickListener {
            finish()
        }
        setCurrentTrack(track = intent.extras?.getSerializable("track") as Track)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    fun setCurrentTrack(track: Track) {
        Glide.with(imageAlbumCover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.default_album_icon)
            .fitCenter()
            .transform(RoundedCorners(8))
            .into(imageAlbumCover)
        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvDuration.text = track.getDuration()
        tvAlbumName.text = track.collectionName
        tvReleaseDate.text = track.getReleaseYear()
        tvPrimaryGenreName.text = track.primaryGenreName
        tvCountry.text = track.country

        preparePlayer(track.previewUrl)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.preparePlayer(
            url,
            onPreparedListener = {
                btnPlay.isEnabled = true
            },
            onCompletionListener = {
                btnPlay.setImageResource(R.drawable.dark_mode_play_icon)
            },
            onPlayerStarted = {
                btnPlay.setImageResource(R.drawable.dark_mode_pause_icon)
                handler.postDelayed(playRunnable, DELAY)
            },
            onPlayerPaused = {
                btnPlay.setImageResource(R.drawable.dark_mode_play_icon)
                handler.removeCallbacks(playRunnable)
                tvCurrentTime.text = String.format("%d:%02d", currentTimeSecs / 60, currentTimeSecs % 60)
            })
    }

    private fun playbackControl() {
        mediaPlayer.playbackControl()
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                currentTimeSecs++
                if (currentTimeSecs < 30) {
                    tvCurrentTime.text = String.format("%d:%02d", currentTimeSecs / 60, currentTimeSecs % 60)
                    handler.postDelayed(this, DELAY)
                } else {
                    currentTimeSecs = 0
                    mediaPlayer.pausePlayer()
                }
            }
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

}