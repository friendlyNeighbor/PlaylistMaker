package com.example.playlistmaker.mvvm.media.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.mvvm.media.ui.favorites.FragmentFavorites
import com.example.playlistmaker.mvvm.media.ui.playlists.FragmentPlaylists

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            FragmentFavorites.newInstance()
        else
            FragmentPlaylists.newInstance()
    }
}
