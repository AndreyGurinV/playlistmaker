package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface CallBackInterface{
    fun setCurrentTrack(track: Track)
    fun getCurrentTrack(): Track
    fun setCurrentPlaylistId(playlistId: String)
    fun getCurrentPlaylistId(): String
    fun setCurrentPlaylist(playlist: PlaylistDto?)
    fun getCurrentPlaylist(): PlaylistDto?
}

class RootActivity : AppCompatActivity(), CallBackInterface {
    private lateinit var binding: ActivityRootBinding
    private lateinit var currentTrack: Track
    private var currentPlaylistId: String = "0"
    private var currentPlaylist: PlaylistDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        val dd = BottomSheetBehavior.from(binding.standardBottomSheet)
        dd.state = BottomSheetBehavior.STATE_HIDDEN

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playlistFragment,
                R.id.newPlaylistFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.playerFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun setCurrentTrack(track: Track) {
        currentTrack = track
    }

    override fun getCurrentTrack(): Track {
        return currentTrack
    }

    override fun setCurrentPlaylistId(playlistId: String) {
        currentPlaylistId = playlistId
    }

    override fun getCurrentPlaylistId(): String {
        return currentPlaylistId
    }

    override fun setCurrentPlaylist(playlist: PlaylistDto?) {
        currentPlaylist = playlist
    }

    override fun getCurrentPlaylist(): PlaylistDto? {
        return currentPlaylist
    }
}