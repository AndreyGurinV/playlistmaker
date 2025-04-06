package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.main.ui.CallBackInterface
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.data.dto.PlaylistInfoDto
import com.example.playlistmaker.media.domain.models.PlaylistFragmentViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TracksAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistFragmentViewModel>()
    private var trackList :ArrayList<Track> = arrayListOf()
    private lateinit var currentPlaylist: PlaylistDto
    lateinit var adapter: TracksAdapter

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlistFragment) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomSheetContainer = binding.bsPlaylistMenu

        val overlay = binding.overlayMenu
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

        binding.btnPlaylistSettings.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.tbBackFromPlaylist.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        isClickAllowed = true
        adapter = TracksAdapter(
            trackList,
            onItemClick = {
                if (clickDebounce()) {
                    (requireActivity() as CallBackInterface).setCurrentTrack(it)
                    findNavController().navigate(
                        R.id.playerFragment
                    )
                }
            },
            onItemLongClick = {
                showAcceptDialog(
                    "Удалить трек",
                    "Вы уверены, что хотите удалить трек из плейлиста?"
                ){
                    viewModel.removeTrack(it)
                }
            }
        )
        binding.rvPlaylistsBS.adapter = adapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            showPlaylist(it)
        }

        binding.tvDeleteMenu.setOnClickListener {
            showAcceptDialog(
                "Удалить плейлист",
                "Хотите удалить плейлист?"
            ){
                viewModel.removePlaylist()
                findNavController().popBackStack()
            }
        }
        binding.tvShareMenu.setOnClickListener {
            sharePlaylist()
        }
        binding.btnPlaylistShare.setOnClickListener {
            sharePlaylist()
        }
        binding.tvEditMenu.setOnClickListener {
            (requireActivity() as CallBackInterface).setCurrentPlaylist(currentPlaylist)
            findNavController().navigate(R.id.newPlaylistFragment)
        }

        setCurrentPlaylist((requireActivity() as CallBackInterface).getCurrentPlaylistId())
    }

    private fun setCurrentPlaylist(id: String){
        viewModel.loadPlaylist(id)
    }

    private fun showPlaylist(playlistInfo: PlaylistInfoDto) {
        currentPlaylist = playlistInfo.playlist
        binding.tvPlaylistTitle.text = playlistInfo.playlist.playlistTitle
        binding.tvPlaylistTitleItemMenu.text = playlistInfo.playlist.playlistTitle
        binding.tvPlaylistDescription.text = playlistInfo.playlist.playlistDescription
        Glide.with(binding.ivPlaylistCover)
            .load(playlistInfo.playlist.pathToImage)
            .placeholder(R.drawable.default_album_icon)
            .transform(RoundedCorners(8))
            .into(binding.ivPlaylistCover)

        Glide.with(binding.ivPlaylistImageItemMenu)
            .load(playlistInfo.playlist.pathToImage)
            .placeholder(R.drawable.default_album_icon)
            .transform(RoundedCorners(8))
            .into(binding.ivPlaylistImageItemMenu)

        binding.tvPlaylistTracksCount.text = formatTracks(playlistInfo.playlist.tracksCount.toInt())
        binding.tvCountTracksItemMenu.text = formatTracks(playlistInfo.playlist.tracksCount.toInt())
        binding.tvPlaylistDuration.text = "${playlistInfo.tracksTime} минут"

        trackList.clear()
        playlistInfo.playlist.tracksIds.split(";").reversed().forEach { id ->
            playlistInfo.tracks.find { it.trackId == id.toInt() }?.let {
                trackList.add(it)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun formatTracks(count: Int): String {
        val lastTwoDigits = count % 100
        val lastDigit = count % 10

        return when {
            lastTwoDigits in 11..19 -> "$count треков"
            lastDigit == 1 -> "$count трек"
            lastDigit in 2..4 -> "$count трека"
            else -> "$count треков"
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun sharePlaylist() {
        if (trackList.isEmpty()) {
            Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_LONG).show()
        } else {
            viewModel.sharePlaylist()
        }
    }

    private fun showAcceptDialog(title: String, mainText: String, onAccepted: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(mainText)
            .setNeutralButton("Нет") { _, _ ->
            }
            .setPositiveButton("Да") { _, _ ->
                onAccepted.invoke()
            }
            .show()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}