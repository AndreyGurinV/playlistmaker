package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel by viewModel<PlayerViewModel>()

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

        binding.btnFavorite.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        findViewById<Toolbar>(R.id.tbBackFromPlayer).setNavigationOnClickListener {
            finish()
        }

        viewModel.observeState().observe(this) {
            binding.btnPlay.isEnabled = it.isPlayButtonEnabled
            if (it.isButtonPaused)
                binding.btnPlay.setImageResource(R.drawable.dark_mode_pause_icon)
            else
                binding.btnPlay.setImageResource(R.drawable.dark_mode_play_icon)
            binding.tvCurrentTime.text = it.progress
        }

        viewModel.observeFavorite().observe(this) {
            if (it)
                binding.btnFavorite.setImageResource(R.drawable.favorites_icon_added)
            else
                binding.btnFavorite.setImageResource(R.drawable.favorite_icon)
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
        binding.btnFavorite.setImageResource(
            if (track.isFavorite)
                R.drawable.favorites_icon_added
            else
                R.drawable.favorite_icon
        )
        viewModel.preparePlayer(track)
    }

}