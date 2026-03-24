package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.api.TrackInteractor

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var buttonClear: ImageView
    private lateinit var recyclerTrackList: RecyclerView
    private lateinit var viewErrorSearch: LinearLayout
    private lateinit var viewErrorConnection: LinearLayout
    private lateinit var buttonReload: View
    private lateinit var viewHistorySearch: LinearLayout
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var progressBar: ProgressBar

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
        progressBar = findViewById(R.id.progressBar)

        searchHistory = SearchHistory(getSharedPreferences(SP_SEARCH_HISTORY, MODE_PRIVATE))

        val buttonBack = findViewById<Toolbar>(R.id.search_back)
        buttonBack.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.edit_text)
        inputEditText.setText(text)

        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

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
                    setViewSearch(PROGRESS)
                    debounceSearchTrack()
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
                debounceSearchTrack()
            }
        }

        tracksAdapter.onTrackClick = { track ->
            if (clickDebounce()) goToAudioPlayer(track)
        }

        historyAdapter.onTrackClick = { track ->
            if (clickDebounce()) goToAudioPlayer(track)
        }

    } // <- end of onCreate

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

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
        trackListHistory.addAll(0, list)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewSearch(reason: String) {
        tracksAdapter.notifyDataSetChanged()
        historyAdapter.notifyDataSetChanged()
        viewErrorSearch.visibility = View.GONE
        viewErrorConnection.visibility = View.GONE
        recyclerTrackList.visibility = View.GONE
        viewHistorySearch.visibility = View.GONE
        progressBar.visibility = View.GONE
        when (reason) {
            CONNECTION_PROBLEM -> viewErrorConnection.visibility = View.VISIBLE
            NOT_FOUND -> viewErrorSearch.visibility = View.VISIBLE
            SEARCH_SUCCESSFUL -> recyclerTrackList.visibility = View.VISIBLE
            HISTORY -> viewHistorySearch.visibility = View.VISIBLE
            PROGRESS -> progressBar.visibility = View.VISIBLE
        }
    }

    private fun debounceSearchTrack() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    val trackInteractor = Creator.provideTrackInteractor(this)
    val searchRunnable = Runnable {searchTrack()}

    fun searchTrack() {
        trackInteractor.searchTrack(
            text,
            object : TrackInteractor.TrackConsumer {
                override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                    handler.post {
                        if (errorMessage != null || foundTrack == null) {
                            setViewSearch(CONNECTION_PROBLEM)
                        }
                        else {
                            trackList.clear()
                            trackList.addAll(foundTrack)
                            if (trackList.isEmpty()) {
                                setViewSearch(NOT_FOUND)
                            }
                            else
                                setViewSearch(SEARCH_SUCCESSFUL)
                        }
                    }
                }
            })
    }

    private fun goToAudioPlayer(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("track", track)
        startActivity(intent)
    }

    companion object {
        private const val TEXT = "TEXT"
        private const val TEXT_DEFAULT = ""

        private const val CONNECTION_PROBLEM = "CONNECTION_PROBLEM"
        private const val NOT_FOUND = "NOT_FOUND"
        private const val SEARCH_SUCCESSFUL = "SEARCH_SUCCESSFUL"
        private const val CLEAR = "CLEAR"
        private const val HISTORY = "HISTORY"
        private const val PROGRESS = "PROGRESS"

        private const val SP_SEARCH_HISTORY = "SP_SEARCH_HISTORY"
        lateinit var searchHistory: SearchHistory
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}