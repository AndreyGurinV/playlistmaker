package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.main.ui.CurrentTrackStorage
import com.example.playlistmaker.media.domain.models.FavoritesFragmentViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TracksAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var trackList :ArrayList<Track> = arrayListOf()
    lateinit var adapter: TracksAdapter
    private var isClickAllowed = true

    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner){
            showFavorites(it)
        }

        adapter = TracksAdapter(trackList) {
            if (clickDebounce()) {
                (requireActivity() as CurrentTrackStorage).setCurrentTrack(it)
                findNavController().navigate(
                    R.id.playerFragment
                )
            }
        }
        binding.rvFavorites.adapter = adapter

        viewModel.loadFavorites()
    }

    private fun showFavorites(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            binding.rvFavorites.isVisible = false
            binding.tvPlaceholder.isVisible = true
            binding.ivPlaceholderNothing.isVisible = true
        } else {
            binding.rvFavorites.isVisible = true
            binding.tvPlaceholder.isVisible = false
            binding.ivPlaceholderNothing.isVisible = false
        }

        trackList.clear()
        trackList.addAll(tracks)
        adapter.notifyDataSetChanged()
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

    companion object {
        fun newInstance() = FavoritesFragment()

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}