package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.media.domain.models.FavoritesFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    companion object {
        fun newInstance() = FavoritesFragment().apply {
        }
    }

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}