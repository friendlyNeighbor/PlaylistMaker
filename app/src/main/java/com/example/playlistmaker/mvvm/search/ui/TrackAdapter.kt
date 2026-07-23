package com.example.playlistmaker.mvvm.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.search.domain.model.Track

class TrackAdapter(val trackList: MutableList<Track>): RecyclerView.Adapter<TrackHolder>() {

    var onTrackClick: ((Track) -> Unit)? = null
    var onTrackLongClick: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
                onTrackClick?.invoke(track)
        }
        holder.itemView.setOnLongClickListener {
            onTrackLongClick?.invoke(track)
            true
        }

    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}