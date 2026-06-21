package com.example.playlistmaker.mvvm.media.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.media.domain.model.Playlist

class PlaylistAdapter(private val listOfPlaylists: List<Playlist>): RecyclerView.Adapter<PlaylistViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
            return PlaylistViewHolder(view)
        }

        override fun getItemCount(): Int {
            return listOfPlaylists.size
        }

        override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
            holder.bind(listOfPlaylists[position])
        }

}