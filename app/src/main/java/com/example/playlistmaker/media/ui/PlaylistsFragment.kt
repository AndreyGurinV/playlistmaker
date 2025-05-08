package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.main.ui.CallBackInterface
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.models.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val viewModel by viewModel<PlaylistsFragmentViewModel>()
    private var playlists :ArrayList<PlaylistDto> = arrayListOf()
    lateinit var adapter: PlaylistsAdapter

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
        }
    }

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner){
            showPlaylists(it)
        }

        binding.btnNewPlaylist.setOnClickListener{
            (requireActivity() as CallBackInterface).setCurrentPlaylist(null)
            findNavController().navigate(R.id.newPlaylistFragment)
        }

        adapter = PlaylistsAdapter(playlists){
            (requireActivity() as CallBackInterface).setCurrentPlaylistId(it.id)
            findNavController().navigate(R.id.playlistFragment)
        }
        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = adapter

        viewModel.load()
    }

    private fun showPlaylists(list: List<PlaylistDto>) {
        if (list.isEmpty()) {
            binding.rvPlaylists.isVisible = false
            binding.ivPlaceholderNothing.isVisible = true
            binding.tvPlaceholder.isVisible = true
        } else {
            binding.rvPlaylists.isVisible = true
            binding.ivPlaceholderNothing.isVisible = false
            binding.tvPlaceholder.isVisible = false
        }
        playlists.clear()
        playlists.addAll(list)
        adapter.notifyDataSetChanged()
    }
}