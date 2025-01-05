package com.example.playlistmaker.ui.tracks

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.impl.SearchHistoryIteractorImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.PlayerActivity


class FindActivity : AppCompatActivity() {
    private var searchText: String = SEARCH_TEXT_DEF
    private var trackList :ArrayList<Track> = arrayListOf()
    lateinit var adapter: TracksAdapter
    private val searchRunnable = Runnable { sendSearchRequest() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private lateinit var etSearch: EditText
    private lateinit var pbSearch: ProgressBar
    private lateinit var btnUpdate: Button
    private lateinit var placeholderMessage: TextView
    private lateinit var imageNothing: ImageView
    private lateinit var imageError: ImageView

    private lateinit var btnClearHistory: Button
    private lateinit var tvSearchHistory: TextView
    private lateinit var searchHistory: SearchHistoryIteractorImpl
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchHistory = Creator.provideSearchHistoryInteractor(context = this)

        findViewById<Toolbar>(R.id.tbBackFromFind).setNavigationOnClickListener {
            finish()
        }

        etSearch = findViewById(R.id.etFind)
        pbSearch = findViewById(R.id.pbSearch)
        val clearButton = findViewById<ImageView>(R.id.ivClear)
        btnUpdate = findViewById(R.id.btnUpdateSearch)
        placeholderMessage = findViewById(R.id.tvPlaceholder)
        imageNothing = findViewById(R.id.ivPlaceholderNothing)
        imageError = findViewById(R.id.ivPlaceholderError)
        btnClearHistory = findViewById(R.id.btnClearSearchHistory)
        tvSearchHistory = findViewById(R.id.tvSearchHistory)

        btnUpdate.setOnClickListener { sendSearchRequest() }
        btnClearHistory.setOnClickListener {
            trackList.clear()
            adapter.notifyDataSetChanged()
            searchHistory.clear()
            showSearchHistory(false)
        }

        clearButton.setOnClickListener {
            etSearch.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(findViewById<LinearLayout>(R.id.main).windowToken, 0)
            showSearchHistory(true)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showSearchHistory (etSearch.hasFocus() && s?.isEmpty() == true)
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                etSearch.setSelection(searchText.length)
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        etSearch.addTextChangedListener(simpleTextWatcher)
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendSearchRequest()
                true
            }
            false
        }
        etSearch.setOnFocusChangeListener { view, hasFocus ->
            showSearchHistory (hasFocus && etSearch.text.isEmpty())
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TracksAdapter(trackList) {
            if (clickDebounce()) {
                searchHistory.addToHistory(track = it)
                if (tvSearchHistory.isVisible) {
                    trackList.clear()
                    trackList.addAll(searchHistory.load())
                    adapter.notifyDataSetChanged()
                }
                val displayIntent = Intent(this@FindActivity, PlayerActivity::class.java)
                displayIntent.putExtra("track", it)
                startActivity(displayIntent)
            }
        }
        recyclerView.adapter = adapter
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

    private fun showMessage(text: String, isError: Boolean) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            trackList.clear()
            adapter.notifyDataSetChanged()
            if (isError) {
                imageError.isVisible = true
                imageNothing.isVisible = false
                placeholderMessage.text = text
                btnUpdate.isVisible = true
            } else {
                imageError.isVisible = false
                imageNothing.isVisible = true
                placeholderMessage.text = text
                btnUpdate.isVisible = false
            }
        } else {
            placeholderMessage.isVisible = false
            imageNothing.isVisible = false
            imageError.isVisible = false
            btnUpdate.isVisible = false
        }
    }

    private fun showSearchHistory(visible: Boolean) {
        trackList.clear()
        if (visible) {
            with(searchHistory.load()) {
                trackList.addAll(this)
                tvSearchHistory.isVisible = isNotEmpty()
                btnClearHistory.isVisible = isNotEmpty()
            }
        } else {
            tvSearchHistory.isVisible = false
            btnClearHistory.isVisible = false
        }
        adapter.notifyDataSetChanged()

    }
    private fun sendSearchRequest() {
        if (etSearch.text.isNotEmpty()) {
            pbSearch.visibility = View.VISIBLE
            Creator.provideTracksInteractor(onError = {
                handler.post {
                    showMessage(getString(R.string.something_went_wrong), true)
                }
            }, onSuccess = {
                handler.post {
                    pbSearch.visibility = View.GONE
                    trackList.clear()
                    trackList.addAll(it)
                    adapter.notifyDataSetChanged()
                    if (it.isEmpty()) {
                        showMessage(getString(R.string.nothing_found), false)
                    } else {
                        showMessage("", false)
                    }
                }
            }).searchTracks(etSearch.text.toString())
        }

    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

