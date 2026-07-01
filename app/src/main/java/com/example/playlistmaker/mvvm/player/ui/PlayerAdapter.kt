package com.example.playlistmaker.mvvm.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.media.domain.model.Playlist


class PlayerAdapter (private val listOfPlaylists: List<Playlist>): RecyclerView.Adapter<PlayerViewHolder>() {

    var onPlaylistClick: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.platlist_view_little, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfPlaylists.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val playlist = listOfPlaylists[position]
        holder.bind(listOfPlaylists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick?.invoke(playlist)
        }
    }
}