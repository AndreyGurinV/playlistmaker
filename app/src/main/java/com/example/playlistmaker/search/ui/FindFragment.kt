package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFindBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.data.TracksState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FindFragment : Fragment() {

    private var searchText: String = SEARCH_TEXT_DEF
    private var trackList :ArrayList<Track> = arrayListOf()
    lateinit var adapter: TracksAdapter
    private var isClickAllowed = true

    private val viewModel by viewModel<TracksSearchViewModel>()
    private lateinit var binding: FragmentFindBinding
    private lateinit var textWatcher: TextWatcher
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.findFragment) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnUpdateSearch.setOnClickListener { sendSearchRequest() }
        binding.btnClearSearchHistory.setOnClickListener {
            viewModel.clear()
            showSearchHistory(false)
        }

        binding.ivClear.setOnClickListener {
            binding.etFind.setText("")
            val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.findFragment.windowToken, 0)
            showSearchHistory(true)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClear.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                binding.etFind.setSelection(searchText.length)
                viewModel.searchDebounce(searchText)
                showSearchHistory (searchText.isEmpty())//binding.etFind.hasFocus() && s?.isEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.etFind.addTextChangedListener(textWatcher)
        binding.etFind.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendSearchRequest()
                true
            }
            false
        }
        binding.etFind.setOnFocusChangeListener { view, hasFocus ->
            showSearchHistory (hasFocus && binding.etFind.text.isEmpty())
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TracksAdapter(trackList) {
            if (clickDebounce()) {
                viewModel.addToHistory(track = it)
                if (binding.tvSearchHistory.isVisible) {
                    viewModel.load()
                }
                val displayIntent = Intent(requireContext(), PlayerActivity::class.java)
                displayIntent.putExtra("track", it)
                startActivity(displayIntent)
            }
        }
        binding.recyclerView.adapter = adapter
        showSearchHistory(true)

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { binding.etFind.removeTextChangedListener(it) }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Empty -> showEmpty(getString(state.messageTextId))
            is TracksState.Error -> showError(getString(state.errorMessageId))
            is TracksState.Loading -> showLoading()
            is TracksState.History -> showHistory(state.tracks)
        }
    }

    private fun showHistory(tracks: List<Track>) {
        if (searchText.isNotEmpty())
            return
        trackList.clear()
        adapter.notifyDataSetChanged()
        hideAllPlaceholders()
        with(tracks) {
            trackList.addAll(this)
            binding.tvSearchHistory.isVisible = isNotEmpty()
            binding.btnClearSearchHistory.isVisible = isNotEmpty()
        }
        adapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>) {
        if (searchText.isEmpty())
            return
        binding.pbSearch.isVisible = false
        trackList.clear()
        trackList.addAll(tracks)
        adapter.notifyDataSetChanged()
        hideAllPlaceholders()
    }

    private fun showEmpty(message: String) {
        binding.tvPlaceholder.isVisible = true
        binding.pbSearch.isVisible = false
        trackList.clear()
        adapter.notifyDataSetChanged()
        binding.ivPlaceholderError.isVisible = false
        binding.ivPlaceholderNothing.isVisible = true
        binding.tvPlaceholder.text = message
        binding.btnUpdateSearch.isVisible = false
    }

    private fun showError(errorMessage: String) {
        binding.tvPlaceholder.isVisible = true
        binding.pbSearch.isVisible = false
        trackList.clear()
        adapter.notifyDataSetChanged()
        binding.ivPlaceholderError.isVisible = true
        binding.ivPlaceholderNothing.isVisible = false
        binding.tvPlaceholder.text = errorMessage
        binding.btnUpdateSearch.isVisible = true
    }

    private fun showLoading() {
        binding.pbSearch.isVisible = true
        hideAllPlaceholders()
    }

    private fun showSearchHistory(visible: Boolean) {
        trackList.clear()
        adapter.notifyDataSetChanged()
        hideAllPlaceholders()
        if (visible) {
            viewModel.load()
        } else {
            binding.tvSearchHistory.isVisible = false
            binding.btnClearSearchHistory.isVisible = false
        }
    }

    private fun hideAllPlaceholders() {
        binding.tvPlaceholder.isVisible = false
        binding.ivPlaceholderNothing.isVisible = false
        binding.ivPlaceholderError.isVisible = false
        binding.btnUpdateSearch.isVisible = false
    }

    private fun sendSearchRequest() {
        if (binding.etFind.text.isNotEmpty()) {
            viewModel.searchDebounce(binding.etFind.text.toString())
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

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}