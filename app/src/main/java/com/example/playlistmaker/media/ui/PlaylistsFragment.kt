package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
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

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistsContent(
                    viewModel.apply { load() },
                    onNewPlaylistClicked = {
                        (requireActivity() as CallBackInterface).setCurrentPlaylist(null)
                        findNavController().navigate(R.id.newPlaylistFragment)
                    }) {
                    (requireActivity() as CallBackInterface).setCurrentPlaylistId(it.id)
                    findNavController().navigate(R.id.playlistFragment)
                }
            }
        }
    }
}