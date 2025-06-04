package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFindBinding
import com.example.playlistmaker.main.ui.CallBackInterface
import com.example.playlistmaker.search.domain.models.TracksSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FindFragment : Fragment() {

    private var searchText: String = SEARCH_TEXT_DEF
    private var isClickAllowed = true

    private val viewModel by viewModel<TracksSearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            isClickAllowed = true
            setContent {
                FindContent(
                    viewModel.apply { load() },
                    onUpdateClicked = {
                        sendSearchRequest()
                    },
                    onTextChanged = {text->
                        searchText = text
                        viewModel.searchDebounce(text)
                    }
                ) {
                    if (clickDebounce()) {
                        viewModel.addToHistory(track = it)
                        (requireActivity() as CallBackInterface).setCurrentTrack(it)
                        findNavController().navigate(
                            R.id.playerFragment
                        )
                    }
                }
            }
        }
    }

    private fun sendSearchRequest() {
        if (searchText.isNotEmpty()) {
            viewModel.searchDebounce(searchText)
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

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
        const val TRACK_BUNDLE = "track"

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}