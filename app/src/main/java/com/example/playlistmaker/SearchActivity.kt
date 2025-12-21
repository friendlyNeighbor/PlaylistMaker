package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class SearchActivity : AppCompatActivity() {
    private val iTunesService = RetrofitClient.iTunesService

    private lateinit var inputEditText: EditText
    private lateinit var buttonClear: ImageView
    private lateinit var recyclerTrackList: RecyclerView
    private lateinit var viewErrorSearch: LinearLayout
    private lateinit var viewErrorConnection: LinearLayout
    private lateinit var buttonReload: View
    private lateinit var viewHistorySearch: LinearLayout
    private lateinit var recyclerHistory: RecyclerView

    private var text: String = TEXT_DEFAULT

    private val trackList: MutableList<Track> = mutableListOf()
    private val tracksAdapter = TrackAdapter(trackList)

    private val trackListHistory: MutableList<Track> = mutableListOf()
    private val historyAdapter = TrackAdapter(trackListHistory)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT, text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            text = savedInstanceState.getString(TEXT, TEXT_DEFAULT)
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerTrackList = findViewById(R.id.recycler)
        recyclerTrackList.adapter = tracksAdapter
        recyclerHistory = findViewById(R.id.recycler_history)
        recyclerHistory.adapter = historyAdapter
        viewErrorSearch = findViewById(R.id.not_found)
        viewErrorConnection = findViewById(R.id.connection_problem)
        viewHistorySearch = findViewById(R.id.history_of_search)
        buttonReload = findViewById(R.id.reload)

        searchHistory = SearchHistory(getSharedPreferences(SP_SEARCH_HISTORY, MODE_PRIVATE))

        val buttonBack = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_back)
        buttonBack.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.edit_text)
        inputEditText.setText(text)

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        buttonClear = findViewById(R.id.clear_search)
        buttonClear.visibility = clearButtonVisibility(text)
        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val buttonClearHistory = findViewById<Button>(R.id.clear_history)
        buttonClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            historyAdapter.notifyDataSetChanged()
            setViewSearch(CLEAR)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = s.toString()
                buttonClear.visibility = clearButtonVisibility(s)
                if (text.isEmpty() && inputEditText.hasFocus()) {
                    trackList.clear()
                    if (searchHistory.getTrackListHistory().isEmpty())
                        setViewSearch(CLEAR)
                    else {
                        readSP()
                        setViewSearch(HISTORY)
                    }
                } else {
                    setViewSearch(CLEAR)
                    inputEditText.setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            searchTrack(text)
                            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                            true
                        } else {
                            false
                        }
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                readSP()
                if (trackListHistory.isNotEmpty())
                    setViewSearch(HISTORY)
            }
        }

        buttonReload.setOnClickListener {
            if (text.isNotEmpty()) {
                searchTrack(text)
            }
        }
    } // <- end of onCreate


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun readSP() {
        val list = searchHistory.getTrackListHistory()
        trackListHistory.clear()
        trackListHistory.addAll(0,list)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewSearch(reason: String) {
        tracksAdapter.notifyDataSetChanged()
        historyAdapter.notifyDataSetChanged()
        viewErrorSearch.visibility = View.GONE
        viewErrorConnection.visibility = View.GONE
        recyclerTrackList.visibility = View.GONE
        viewHistorySearch.visibility = View.GONE

        when (reason) {
            CONNECTION_PROBLEM -> viewErrorConnection.visibility = View.VISIBLE
            NOT_FOUND -> viewErrorSearch.visibility = View.VISIBLE
            SEARCH_SUCCESSFUL -> recyclerTrackList.visibility = View.VISIBLE
            HISTORY -> viewHistorySearch.visibility = View.VISIBLE
        }
    }

    private fun searchTrack(query: String) {
        iTunesService.searchSong(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    trackList.clear()
                    body?.results?.forEach { result ->
                        val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format(result.trackTimeMillis)
                        trackList.add(
                            Track(
                                result.trackName,
                                result.artistName,
                                formattedTime,
                                result.artworkUrl100,
                                result.trackId
                            )
                        )
                    }
                    if (trackList.isEmpty()) {
                        setViewSearch(NOT_FOUND)
                    } else {
                        setViewSearch(SEARCH_SUCCESSFUL)
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                setViewSearch(CONNECTION_PROBLEM)
            }
        })
    }

    companion object {
        private const val TEXT = "TEXT"
        private const val TEXT_DEFAULT = ""

        private const val CONNECTION_PROBLEM = "CONNECTION_PROBLEM"
        private const val NOT_FOUND = "NOT_FOUND"
        private const val SEARCH_SUCCESSFUL = "SEARCH_SUCCESSFUL"
        private const val CLEAR = "CLEAR"
        private const val HISTORY = "HISTORY"

        private const val SP_SEARCH_HISTORY = "SP_SEARCH_HISTORY"
        lateinit var searchHistory: SearchHistory
    }
}