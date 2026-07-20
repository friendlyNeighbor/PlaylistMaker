package com.example.playlistmaker.mvvm.media.ui.createPlaylist


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class CreatePlaylistViewModel(
    private val imageSaverInteractor: ImageSaverInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val createPlaylistLiveData = MutableLiveData<StateCreate>()
    fun getLiveData(): LiveData<StateCreate> = createPlaylistLiveData

    private lateinit var editingPlaylist: Playlist
    var uriImage: Uri? = null

    fun refreshImage(uri: Uri?) {
        uriImage = uri
        createPlaylistLiveData.postValue(StateCreate(null, null, uriImage, false))
    }

    fun savePlaylist(textTitle: String, textDescription: String) {
        viewModelScope.launch {
                val newId = playlistInteractor.addNewPlaylist(Playlist(0L, textTitle, textDescription, emptyList(), null))
                val currentUri = uriImage
                if (currentUri != null) {
                    imageSaverInteractor.saveImage(currentUri, newId)
                }
            createPlaylistLiveData.postValue(StateCreate(null, null, null, true))

        }
    }

    fun loadPlaylistById(id: Long) {
        viewModelScope.launch {
            editingPlaylist = playlistInteractor.getPlaylistById(id).first()
            val id= editingPlaylist.id
            uriImage = imageSaverInteractor.getImage(id)
            createPlaylistLiveData.postValue(StateCreate(editingPlaylist.title, editingPlaylist.description, uriImage, false))
        }
    }

    fun updatePlaylist(textTitle: String, textDescription: String) {
            viewModelScope.launch {
                editingPlaylist.title = textTitle
                editingPlaylist.description = textDescription
                playlistInteractor.updatePlaylist(editingPlaylist)

                val id = editingPlaylist.id
                val editingUriImage = imageSaverInteractor.getImage(id)
                val currentUri = uriImage

                if (currentUri != null && currentUri!=editingUriImage) {
                    imageSaverInteractor.saveImage(currentUri, id)
                }
                createPlaylistLiveData.postValue(StateCreate(null, null, null, true))
            }
        }
}

