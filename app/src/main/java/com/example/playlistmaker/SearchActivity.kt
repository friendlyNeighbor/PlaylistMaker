package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
    private lateinit var viewTracks: RecyclerView
    private lateinit var viewErrorSearch: LinearLayout
    private lateinit var viewErrorConnection: LinearLayout
    private lateinit var buttonReload: View

    private var text:String = TEXT_DEFAULT
    private val trackList:MutableList<Track> = mutableListOf()
    private val tracksAdapter = TrackAdapter(trackList)

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
        viewTracks = findViewById(R.id.recycler)
        viewTracks.adapter = tracksAdapter
        viewErrorSearch = findViewById(R.id.not_found)
        viewErrorConnection = findViewById(R.id.connection_problem)
        buttonReload = findViewById(R.id.reload)


        val buttonBack = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_back)
        buttonBack.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.edit_text)
        inputEditText.setText(text)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        buttonClear = findViewById(R.id.clear_search)
        buttonClear.visibility = clearButtonVisibility(text)
        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                text=s.toString()
                buttonClear.visibility = clearButtonVisibility(s)
                if (text.isEmpty()) {
                    trackList.clear()
                    setViewSearch(CLEAR)
                }
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (text.isNotEmpty()) {
                    searchTrack(text)
                }
                inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                true
            } else {
                false
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewSearch(reason: String) {
        tracksAdapter.notifyDataSetChanged()
        viewErrorSearch.visibility = View.GONE
        viewErrorConnection.visibility = View.GONE
        viewTracks.visibility = View.GONE
        when (reason) {
            "CONNECTION_PROBLEM" -> viewErrorConnection.visibility = View.VISIBLE

            "NOT_FOUND" -> viewErrorSearch.visibility = View.VISIBLE

            "OK" -> viewTracks.visibility = View.VISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredText = savedInstanceState.getString("search_text", "")
        inputEditText.setText(restoredText)
    }

    private fun searchTrack(query: String) {
        iTunesService.searchSong(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    trackList.clear()
                    body?.results?.forEach { result ->
                        val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format(result.trackTimeMillis)
                        trackList.add(
                            Track(result.trackName, result.artistName, formattedTime, result.artworkUrl100 )                  )
                    }
                    if (trackList.isEmpty()) {
                        setViewSearch(NOT_FOUND)
                    } else {
                        setViewSearch(OK)
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
        private const val OK = "OK"
        private const val CLEAR = "CLEAR"
    }
}