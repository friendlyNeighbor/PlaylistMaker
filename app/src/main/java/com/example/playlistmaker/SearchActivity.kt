package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var text:String = TEXT_DEFAULT

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT, text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContentView(R.layout.activity_search)

        val tracks: RecyclerView = findViewById(R.id.recycler)
        tracks.adapter = tracksAdapter

        val buttonBack = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_back)
        buttonBack.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            text = savedInstanceState.getString(TEXT, TEXT_DEFAULT)
        }

        val inputEditText = findViewById<EditText>(R.id.edit_text)
        inputEditText.setText(text)

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        val clearButton = findViewById<ImageView>(R.id.clear_search)
        clearButton.visibility = clearButtonVisibility(text)
        clearButton.setOnClickListener {
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
                clearButton.visibility = clearButtonVisibility(s)
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    companion object {
        private const val TEXT = "TEXT"
        private const val TEXT_DEFAULT = ""
    }

    private val track1=Track(
         "Smells Like Teen Spirit",
         "Nirvana",
         "5:01",
        "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
    )
    private val track2=Track(
        "Billie Jean",
        "Michael Jackson",
        "4:35",
        "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
    )
    private val track3=Track(
        "Stayin' Alive",
        "Bee Gees",
        "4:10",
        "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
    )
    private val track4=Track(
        "Whole Lotta Love",
        "Led Zeppelin",
        "5:33",
        "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
    )
    private val track5=Track(
        "Sweet Child O'Mine",
        "Guns N' Roses",
        "5:03",
        "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
    )
    private val trackList:MutableList<Track> = mutableListOf(track1, track2, track3, track4, track5)
    private val tracksAdapter = TrackAdapter(trackList)

}