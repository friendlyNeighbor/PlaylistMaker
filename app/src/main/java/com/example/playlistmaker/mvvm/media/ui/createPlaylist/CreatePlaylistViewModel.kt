package com.example.playlistmaker.mvvm.media.ui.createPlaylist


import android.net.Uri
import android.util.Log
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

//    private lateinit var editingPlaylist: Playlist
    private var uriImage: Uri? = null

  //  private var savingComplete = true
//    private var listId: List<Long> = emptyList()

    fun refreshImage(uri: Uri?) {
        uriImage = uri
        createPlaylistLiveData.postValue(StateCreate(null, null, uriImage, false))
    }


    fun savePlaylist(textTitle: String, textDescription: String) {
        Log.d("MyError", "     viewModel - savePlaylist ")
        viewModelScope.launch {
            Log.d("MyError", "     viewModel - savePlaylist(Scope start))")
                val newId = playlistInteractor.addNewPlaylist(Playlist(0L, textTitle, textDescription, emptyList(), null))
                val currentUri = uriImage
                if (currentUri != null) {
                   // savingComplete = false
                    Log.d("MyError", " currentUri!=null, пробуем сохранить, id=$newId, uri=$currentUri")

                    val savingComplete = imageSaverInteractor.saveImage(currentUri, newId)
                    Log.d("MyError", "     viewModel Scope - savingCompl = $savingComplete")
                }
            Log.d("MyError", "     viewModel - savePlaylist(Scope END))")
            createPlaylistLiveData.postValue(StateCreate(null, null, null, true))

        }

        Log.d("MyError", "     viewModel - savePlaylist END")
    }
/*
    fun closeFragment() {
        Log.d("MyError", "           ViewModel closeFrag")
        if(savingComplete)
            createPlaylistLiveData.postValue(StateCreate(null, null, null, true))
    }


 */

/*

    fun updatePlaylist(textTitle: String, textDescription: String) {
        Log.d("MyError", "updatePlaylist")
        viewModelScope.launch {
            editingPlaylist.title = textTitle
            editingPlaylist.description = textDescription
            val id = editingPlaylist.id
            playlistInteractor.updatePlaylist(editingPlaylist)
            uriImage?.let { imageSaverInteractor.saveImage(it, id) }
        }
    }

    fun loadPlaylistById(id: Long) {
        Log.d("MyError", "loadPlaylistById(CreatePlaylist)")
        viewModelScope.launch {
            editingPlaylist = playlistInteractor.getPlaylistById(id).first()
            val id= editingPlaylist.id
            uriImage = imageSaverInteractor.getImage(id)
            createPlaylistLiveData.postValue(StateCreate(editingPlaylist.title, editingPlaylist.description, uriImage))
        }
    }


 */
}

