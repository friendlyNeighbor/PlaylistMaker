package com.example.playlistmaker.mvvm.player.ui

sealed class PlayingStatus(val progress: String) {

    class Default : PlayingStatus("00:00")

    class Prepared : PlayingStatus("00:00")

    class Complitted : PlayingStatus("00:00")

    class Playing(progress: String) :PlayingStatus(progress)

    class Paused(progress: String) : PlayingStatus(progress)
}