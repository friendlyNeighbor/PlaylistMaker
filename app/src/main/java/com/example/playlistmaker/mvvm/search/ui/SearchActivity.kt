package com.example.playlistmaker.mvvm.search.ui

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.mvvm.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.mvvm.search.domain.SearchState
import com.example.playlistmaker.mvvm.search.domain.SearchStatus
import com.example.playlistmaker.mvvm.search.domain.SearchViewModel
import com.example.playlistmaker.presentation.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {

    // изменить логику text

    private lateinit var viewModel: SearchViewModel

    private lateinit var inputEditText: EditText
    private lateinit var buttonClear: ImageView
    private lateinit var recyclerTrackList: RecyclerView
    private lateinit var viewErrorSearch: LinearLayout
    private lateinit var viewErrorConnection: LinearLayout
    private lateinit var buttonReload: View
    private lateinit var viewHistorySearch: LinearLayout
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonBack: Toolbar
    private lateinit var buttonClearHistory: Button

    private val trackList: MutableList<Track> = mutableListOf()
    private val tracksAdapter = TrackAdapter(trackList)

    private val trackListHistory: MutableList<Track> = mutableListOf()
    private val historyAdapter = TrackAdapter(trackListHistory)

    private var text: String = TEXT_DEFAULT

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
        recyclerHistory = findViewById(R.id.recycler_history)
        viewErrorSearch = findViewById(R.id.not_found)
        viewErrorConnection = findViewById(R.id.connection_problem)
        viewHistorySearch = findViewById(R.id.history_of_search)
        buttonReload = findViewById(R.id.reload)
        progressBar = findViewById(R.id.progressBar)
        buttonBack = findViewById(R.id.search_back)
        inputEditText = findViewById(R.id.edit_text)
        buttonClear = findViewById(R.id.clear_search)
        buttonClearHistory = findViewById(R.id.clear_history)

        recyclerTrackList.adapter = tracksAdapter
        recyclerHistory.adapter = historyAdapter

        tracksAdapter.onTrackClick = { track ->
            if (clickDebounce()) {
                goToAudioPlayer(track)
            }
        }

        historyAdapter.onTrackClick = { track ->
            if (clickDebounce()) {
                goToAudioPlayer(track)
            }
        }

        val primaryState = SearchState(SearchStatus.CLEAR, trackList)
        viewModel = ViewModelProvider(this, SearchViewModel.getFactory(primaryState))
            .get(SearchViewModel::class.java)

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        searchHistory = Creator.provideSearchHistoryInteractor(App.instance.applicationContext)

        buttonBack.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text=s.toString()
                viewModel.textWasChanged(text)
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.editTextInFocus()
                viewModel.textWasChanged(text)
            }
        }
        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        buttonReload.setOnClickListener {
            viewModel.textWasChanged(text)
        }


        viewModel.getLiveData().observe(this) {
           if(it.searchStatus == SearchStatus.HISTORY) {
               trackListHistory.clear()
               trackListHistory.addAll(it.searchResult)
           }
           else {
               trackList.clear()
               trackList.addAll(it.searchResult)
           }
            setViewSearch(it.searchStatus)
        }

    } // end of onCreate()

    private fun setViewSearch(reason: SearchStatus) {

        tracksAdapter.notifyDataSetChanged()
        historyAdapter.notifyDataSetChanged()

        viewErrorSearch.visibility = View.GONE
        viewErrorConnection.visibility = View.GONE
        recyclerTrackList.visibility = View.GONE
        viewHistorySearch.visibility = View.GONE
        progressBar.visibility = View.GONE
        buttonClear.visibility = View.VISIBLE

        when (reason) {
            SearchStatus.CONNECTION_PROBLEM -> viewErrorConnection.visibility = View.VISIBLE
            SearchStatus.NOT_FOUND -> viewErrorSearch.visibility = View.VISIBLE
            SearchStatus.SEARCH_SUCCESSFUL -> recyclerTrackList.visibility = View.VISIBLE
            SearchStatus.HISTORY -> {
                viewHistorySearch.visibility = View.VISIBLE
                buttonClear.visibility = View.GONE
            }
            SearchStatus.PROGRESS ->  progressBar.visibility = View.VISIBLE
            SearchStatus.CLEAR ->  buttonClear.visibility = View.GONE
        }
    }
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

    private fun goToAudioPlayer(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("track", track)
        startActivity(intent)
    }

    companion object {
        private const val TEXT = "TEXT"
        private const val TEXT_DEFAULT = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        lateinit var searchHistory: SearchHistoryInteractor
    }
}


