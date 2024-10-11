package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
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

class FindActivity : AppCompatActivity() {
    private var searchText: String = SEARCH_TEXT_DEF
    private var trackList :ArrayList<Track> = arrayListOf()
    lateinit var adapter: TracksAdapter

    private lateinit var inputEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var placeholderMessage: TextView
    private lateinit var imageNothing: ImageView
    private lateinit var imageError: ImageView

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

        findViewById<Toolbar>(R.id.tbBackFromFind).setNavigationOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.etFind)
        val clearButton = findViewById<ImageView>(R.id.ivClear)
        updateButton = findViewById(R.id.btnUpdateSearch)
        placeholderMessage = findViewById(R.id.tvPlaceholder)
        imageNothing = findViewById(R.id.ivPlaceholderNothing)
        imageError = findViewById(R.id.ivPlaceholderError)

        updateButton.setOnClickListener { sendSearchRequest() }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(findViewById<LinearLayout>(R.id.main).windowToken, 0)
            trackList.clear()
            adapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                inputEditText.setSelection(searchText.length)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendSearchRequest()
                true
            }
            false
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TracksAdapter(trackList)
        recyclerView.adapter = adapter

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
                updateButton.isVisible = true
            } else {
                imageError.isVisible = false
                imageNothing.isVisible = true
                placeholderMessage.text = text
                updateButton.isVisible = false
            }
        } else {
            placeholderMessage.isVisible = false
            imageNothing.isVisible = false
            imageError.isVisible = false
            updateButton.isVisible = false
        }
    }

    private fun sendSearchRequest() {
        if (inputEditText.text.isNotEmpty()) {
            itunesService.search(inputEditText.text.toString()).enqueue(object :
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

