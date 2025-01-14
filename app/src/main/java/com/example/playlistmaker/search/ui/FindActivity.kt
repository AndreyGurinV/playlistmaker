package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.data.TracksState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksSearchViewModel


class FindActivity : AppCompatActivity() {
    private var searchText: String = SEARCH_TEXT_DEF
    private var trackList :ArrayList<Track> = arrayListOf()
    lateinit var adapter: TracksAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private lateinit var viewModel: TracksSearchViewModel
    private lateinit var binding: ActivityFindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, TracksSearchViewModel.getViewModelFactory())[TracksSearchViewModel::class.java]

        findViewById<Toolbar>(R.id.tbBackFromFind).setNavigationOnClickListener {
            finish()
        }

        binding.btnUpdateSearch.setOnClickListener { sendSearchRequest() }
        binding.btnClearSearchHistory.setOnClickListener {
            trackList.clear()
            adapter.notifyDataSetChanged()
            viewModel.clear()
            showSearchHistory(false)
        }

        binding.ivClear.setOnClickListener {
            binding.etFind.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(findViewById<LinearLayout>(R.id.main).windowToken, 0)
            showSearchHistory(true)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showSearchHistory (binding.etFind.hasFocus() && s?.isEmpty() == true)
                binding.ivClear.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                binding.etFind.setSelection(searchText.length)
                viewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.etFind.addTextChangedListener(simpleTextWatcher)
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
//        recyclerView = findViewById(R.id.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TracksAdapter(trackList) {
            if (clickDebounce()) {
                viewModel.addToHistory(track = it)
                if (binding.tvSearchHistory.isVisible) {
                    trackList.clear()
                    trackList.addAll(viewModel.load())
                    adapter.notifyDataSetChanged()
                }
                val displayIntent = Intent(this@FindActivity, PlayerActivity::class.java)
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT, SEARCH_TEXT_DEF)
            findViewById<EditText>(R.id.etFind).apply {
                setText(searchText)
                setSelection(searchText.length)
            }
        }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Empty -> showEmpty(state.message)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Loading -> showLoading()
        }
    }

    private fun showContent(tracks: List<Track>) {
        binding.pbSearch.isVisible = false
        trackList.clear()
        trackList.addAll(tracks)
        adapter.notifyDataSetChanged()
        binding.tvPlaceholder.isVisible = false
        binding.ivPlaceholderNothing.isVisible = false
        binding.ivPlaceholderError.isVisible = false
        binding.btnUpdateSearch.isVisible = false
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
        binding.tvPlaceholder.isVisible = false
        binding.ivPlaceholderNothing.isVisible = false
        binding.ivPlaceholderError.isVisible = false
        binding.btnUpdateSearch.isVisible = false
    }

    private fun showSearchHistory(visible: Boolean) {
        trackList.clear()
        if (visible) {
            with(viewModel.load()) {
                trackList.addAll(this)
                binding.tvSearchHistory.isVisible = isNotEmpty()
                binding.btnClearSearchHistory.isVisible = isNotEmpty()
            }
        } else {
            binding.tvSearchHistory.isVisible = false
            binding.btnClearSearchHistory.isVisible = false
        }
        adapter.notifyDataSetChanged()

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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

