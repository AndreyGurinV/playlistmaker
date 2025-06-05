package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.CallBackInterface
import com.example.playlistmaker.media.domain.models.FavoritesFragmentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var isClickAllowed = true

    private val viewModel by viewModel<FavoritesFragmentViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            isClickAllowed = true
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FavoritesContent(viewModel.apply {
                    loadFavorites()
                }){
                    if (clickDebounce()) {
                        (requireActivity() as CallBackInterface).setCurrentTrack(it)
                        findNavController().navigate(
                            R.id.playerFragment
                        )
                    }
                }
            }
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
        fun newInstance() = FavoritesFragment()

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}