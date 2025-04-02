package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface CallBackInterface{
    fun showMessage(message: String)
}

interface CurrentTrackStorage{
    fun setCurrentTrack(track: Track)
    fun getCurrentTrack(): Track
}

class RootActivity : AppCompatActivity(), CallBackInterface, CurrentTrackStorage {
    private lateinit var binding: ActivityRootBinding
    private lateinit var currentTrack: Track

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

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

//        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
//        binding.tvBottomSheetText.text = message
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_HIDDEN)
//                    binding.bottomNavigationView.visibility = View.VISIBLE
//                else
//                    binding.bottomNavigationView.visibility = View.GONE
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            }
//
//        })
    }

    override fun setCurrentTrack(track: Track) {
        currentTrack = track
    }

    override fun getCurrentTrack(): Track {
        return currentTrack
    }
}