package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.domain.models.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val handler = Handler(Looper.getMainLooper())
    private val playRunnable = createUpdateTimerTask()

    private val viewModel by viewModel<PlayerViewModel>()

    var currentTimeSecs = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        findViewById<Toolbar>(R.id.tbBackFromPlayer).setNavigationOnClickListener {
            finish()
        }
        viewModel.observeState().observe(this) {
            render(it)
        }

        setCurrentTrack(track = intent.extras?.getSerializable("track") as Track)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    fun setCurrentTrack(track: Track) {
        Glide.with(binding.ivAlbumCover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.default_album_icon)
            .fitCenter()
            .transform(RoundedCorners(8))
            .into(binding.ivAlbumCover)
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvProgress.text = track.getDuration()
        binding.tvAlbum.text = track.collectionName
        binding.tvReleaseDate.text = track.getReleaseYear()
        binding.tvPrimaryGenreName.text = track.primaryGenreName
        binding.tvCountry.text = track.country

        preparePlayer(track.previewUrl)
    }

    private fun preparePlayer(url: String) {
        viewModel.preparePlayer(url)
    }

    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.Prepared -> {
                binding.btnPlay.isEnabled = true
            }
            is PlayerState.Completion -> {
                binding.btnPlay.setImageResource(R.drawable.dark_mode_play_icon)
            }
            is PlayerState.Paused -> {
                binding.btnPlay.setImageResource(R.drawable.dark_mode_play_icon)
                handler.removeCallbacks(playRunnable)
                binding.tvCurrentTime.text = String.format("%d:%02d", currentTimeSecs / 60, currentTimeSecs % 60)
            }
            is PlayerState.Started -> {
                binding.btnPlay.setImageResource(R.drawable.dark_mode_pause_icon)
                handler.postDelayed(playRunnable, DELAY)
            }
        }
    }


    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                currentTimeSecs++
                if (currentTimeSecs < 30) {
                    binding.tvCurrentTime.text = String.format("%d:%02d", currentTimeSecs / 60, currentTimeSecs % 60)
                    handler.postDelayed(this, DELAY)
                } else {
                    currentTimeSecs = 0
                    viewModel.pausePlayer()
                }
            }
        }
    }

    companion object {
        private const val DELAY = 1000L
    }
}