package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PLAY_LIST_PREFERENCES = "play_list_preferences"

class FindActivity : AppCompatActivity() {
    private var searchText: String = SEARCH_TEXT_DEF
    private var trackList :ArrayList<Track> = arrayListOf()
    lateinit var adapter: TracksAdapter

    private lateinit var etSearch: EditText
    private lateinit var btnUpdate: Button
    private lateinit var placeholderMessage: TextView
    private lateinit var imageNothing: ImageView
    private lateinit var imageError: ImageView

    private lateinit var btnClearHistory: Button
    private lateinit var tvSearchHistory: TextView
    private lateinit var searchHistory: SearchHistory
    private lateinit var recyclerView: RecyclerView

    private val itunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        searchHistory = SearchHistory(getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE))

        findViewById<Toolbar>(R.id.tbBackFromFind).setNavigationOnClickListener {
            finish()
        }

        etSearch = findViewById(R.id.etFind)
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
            if (!tvSearchHistory.isVisible)
                searchHistory.addToHistory(track = it)
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
//        showSearchHistory(false)
    }

    private fun showSearchHistory(visible: Boolean) {
        trackList.clear()
        if (visible) {
            val ss = searchHistory.load()
            println("loaded list = ${ss.size}")
            trackList.addAll(ss)
        }
        adapter.notifyDataSetChanged()

        tvSearchHistory.isVisible = visible
        btnClearHistory.isVisible = visible
    }
    private fun sendSearchRequest() {
        if (etSearch.text.isNotEmpty()) {
            itunesService.search(etSearch.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    if (response.code() == REQUEST_OK) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), false)
                        } else {
                            showMessage("", false)
                        }
                    } else {
                        showMessage(getString(R.string.something_went_wrong), true)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(getString(R.string.something_went_wrong), true)
                }

            })
        }

    }

    companion object {
        const val REQUEST_OK = 200
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }
}

