package com.example.playlistmaker.mvvm.search.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.mvvm.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val trackList: MutableList<Track> = mutableListOf()
    private val tracksAdapter = TrackAdapter(trackList)

    private val trackListHistory: MutableList<Track> = mutableListOf()
    private val historyAdapter = TrackAdapter(trackListHistory)

    private var text: String = TEXT_DEFAULT

    private val primaryState = SearchState(SearchStatus.CLEAR, trackList)
    private val viewModel: SearchViewModel by viewModel() {
        parametersOf(primaryState)
    }

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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recycler.adapter = tracksAdapter
        binding.recyclerHistory.adapter = historyAdapter

        tracksAdapter.onTrackClick = { track ->
            if (clickDebounce()) {
                viewModel.goToPlayer(track, this@SearchActivity)
            }
        }

        historyAdapter.onTrackClick = { track ->
            if (clickDebounce()) {
                viewModel.goToPlayer(track, this@SearchActivity)
            }
        }

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.searchBack.setOnClickListener {
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
        binding.editText.addTextChangedListener(simpleTextWatcher)
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.editTextInFocus()
                viewModel.textWasChanged(text)
            }
        }
        binding.clearSearch.setOnClickListener {
            binding.editText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(binding.editText.windowToken, 0)
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.reload.setOnClickListener {
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

    binding.apply {
        notFound.visibility = View.GONE
        connectionProblem.visibility = View.GONE
        recycler.visibility = View.GONE
        historyOfSearch.visibility = View.GONE
        progressBar.visibility = View.GONE
        clearSearch.visibility = View.VISIBLE
    }
        when (reason) {
            SearchStatus.CONNECTION_PROBLEM -> binding.connectionProblem.visibility = View.VISIBLE
            SearchStatus.NOT_FOUND -> binding.notFound.visibility = View.VISIBLE
            SearchStatus.SEARCH_SUCCESSFUL -> binding.recycler.visibility = View.VISIBLE
            SearchStatus.HISTORY -> {
                binding.historyOfSearch.visibility = View.VISIBLE
                binding.clearSearch.visibility = View.GONE
            }
            SearchStatus.PROGRESS ->  binding.progressBar.visibility = View.VISIBLE
            SearchStatus.CLEAR ->  binding.clearSearch.visibility = View.GONE
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

    companion object {
        private const val TEXT = "TEXT"
        private const val TEXT_DEFAULT = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}


