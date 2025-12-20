package com.example.playlistmaker
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val trackList: MutableList<Track>): RecyclerView.Adapter<TrackHolder>() {

    var onTrackClick: ((Track) -> Unit)? = null

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder( holder: TrackHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
                onTrackClick?.invoke(track)
                SearchActivity.searchHistory.addTrackInHistory(track)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}
