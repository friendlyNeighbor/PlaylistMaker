package com.example.playlistmaker.mvvm.player.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.media.domain.model.Playlist

class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = itemView.findViewById(R.id.title)
    private val numberOfTracks: TextView = itemView.findViewById(R.id.numberOfTracks)
    private val cover: com.google.android.material.imageview.ShapeableImageView = itemView.findViewById(R.id.cover)

    fun bind(playlist: Playlist) {
        title.text = playlist.title
        val trackCount: Int = playlist.idListTracks.size
        val numbers = if (trackCount == 0) {
            "0 треков"
        } else {
            itemView.context.resources.getQuantityString(R.plurals.track_count, trackCount, trackCount)
        }
        numberOfTracks.text = numbers
        if(playlist.uriImage!=null)
            cover.setImageURI(playlist.uriImage)
    }
}
