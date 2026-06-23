package com.example.playlistmaker.mvvm.media.ui.createPlaylist


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.model.Playlist


class CreatePlaylistViewModel(private val imageSaverInteractor: ImageSaverInteractor, private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val createPlaylistLiveData = MutableLiveData<StateCreate>()
    fun getLiveData():LiveData<StateCreate> = createPlaylistLiveData

    private var uriImage:Uri? = null
    fun savePlaylist(textTitle: String, textDescription: String, uriImage: Uri?) {
        playlistInteractor.addNewPlaylist(Playlist(textTitle, textDescription,emptyList() ,null))
        if(uriImage!=null) {
            imageSaverInteractor.saveImage(uriImage, textTitle)
    }
        }

    fun pickImage(uri:Uri) {
        uriImage = uri
        createPlaylistLiveData.postValue(StateCreate(uriImage))
    }
}
