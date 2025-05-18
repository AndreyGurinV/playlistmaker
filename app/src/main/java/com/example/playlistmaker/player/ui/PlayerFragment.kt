package com.example.playlistmaker.player.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.main.ui.CallBackInterface
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.player.domain.models.PlayerViewModel
import com.example.playlistmaker.player.service.MusicService
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment() : Fragment() {

    private lateinit var binding: FragmentPlayerBinding

    private val viewModel by viewModel<PlayerViewModel>()
    private var playlists :ArrayList<PlaylistDto> = arrayListOf()
    lateinit var adapter: PlaylistsAdapterBS

    lateinit var currrentTrack: Track
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), "Can't start foreground service!", Toast.LENGTH_LONG).show()
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.playerFragment) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomSheetContainer = binding.playlistsBottomSheet

        val overlay = binding.overlay
        overlay.visibility = View.GONE

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.btnAdd.setOnClickListener {
            viewModel.load()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.btnPlay.click = {
            viewModel.playbackControl()
        }

        binding.btnFavorite.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding.btnNewPlaylistOnBottomSheet.setOnClickListener {
            (requireActivity() as CallBackInterface).setCurrentPlaylist(null)
            findNavController().navigate(R.id.newPlaylistFragment)
        }

        binding.tbBackFromPlayer.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            binding.btnPlay.isEnabled = it.isPlayButtonEnabled
            if (it.isButtonPaused)
                binding.btnPlay.changeState(true)
            else
                binding.btnPlay.changeState(false)
            binding.tvCurrentTime.text = it.progress
        }

        viewModel.observeFavorite().observe(viewLifecycleOwner) {
            if (it)
                binding.btnFavorite.setImageResource(R.drawable.favorites_icon_added)
            else
                binding.btnFavorite.setImageResource(R.drawable.favorite_icon)
        }

        viewModel.observePlaylists().observe(viewLifecycleOwner){
            showPlaylists(it)
        }

        viewModel.observeMessages().observe(viewLifecycleOwner){
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        setCurrentTrack((requireActivity() as CallBackInterface).getCurrentTrack())

        adapter = PlaylistsAdapterBS(playlists) {
            viewModel.addTackToPlaylist(it)
        }
        binding.rvPlaylistsBS.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlaylistsBS.adapter = adapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }
    }

    override fun onPause() {
        viewModel.showNotification()
        super.onPause()
    }

    override fun onResume() {
        viewModel.hideNotification()
        super.onResume()
    }
    override fun onDestroy() {
        unbindMusicService()
        super.onDestroy()
    }

    fun setCurrentTrack(track: Track) {
        currrentTrack = track
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

    private fun showPlaylists(list: List<PlaylistDto>) {
        if (list.isEmpty()) {
            binding.rvPlaylistsBS.isVisible = false
        } else {
            binding.rvPlaylistsBS.isVisible = true
        }
        playlists.clear()
        playlists.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            putExtra("song_url", currrentTrack.previewUrl)
            putExtra("artist_name", currrentTrack.artistName)
            putExtra("track_name", currrentTrack.trackName)
        }

        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }
}