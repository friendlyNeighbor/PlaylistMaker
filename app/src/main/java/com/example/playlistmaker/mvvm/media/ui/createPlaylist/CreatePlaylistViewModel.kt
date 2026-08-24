package com.example.playlistmaker.mvvm.media.ui.createPlaylist


import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mvvm.media.domain.api.ImageSaverInteractor
import com.example.playlistmaker.mvvm.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CreatePlaylistViewModel(
    savedStateHandle: SavedStateHandle,
    private val imageSaverInteractor: ImageSaverInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(StateCreate("", "", null, false))
    val state: StateFlow<StateCreate> = _state.asStateFlow()

    val playlistId: Long? = savedStateHandle.get<Long>("playlistId")?.takeIf { it != -1L }
    private lateinit var editingPlaylist: Playlist

    var mode: String
    val EDITING = "EDITING"
    val CREATING = "CREATING"

    init {
        if(playlistId!=null) {
            mode = EDITING
            viewModelScope.launch {
                editingPlaylist = playlistInteractor.getPlaylistById(playlistId).first()
                val uriImage = imageSaverInteractor.getImage(playlistId)

                _state.update { it.copy(
                    title = editingPlaylist.title,
                    description = editingPlaylist.description,
                    uri = uriImage,
                    modeEditing = true
                    )
                }
            }

        }
        else {
            mode = CREATING
            _state.update { it.copy(
                modeEditing = false
                )
            }
        }
    }

    fun onTextTitleChange(newText: String) {
        _state.update { it.copy(
            title = newText,
        )
        }
    }

    fun onTextDescriptionChange(newText: String) {
        _state.update { it.copy(
            description = newText,
        )
        }
    }

    fun savePlaylist() {
        if(mode == CREATING)
            createPlaylist()
        else
            updatePlaylist()
    }

    private fun createPlaylist() {
        viewModelScope.launch {
            val newId = playlistInteractor.addNewPlaylist(
                Playlist(0L,
                    _state.value.title,
                    _state.value.description,
                    emptyList(),
                    null))
            if (_state.value.uri != null) {
                imageSaverInteractor.saveImage(_state.value.uri!!, newId)
            }
            _state.update { it.copy(savingComplete = true) }
        }
    }

    private fun updatePlaylist() {
        viewModelScope.launch {
            editingPlaylist.title = _state.value.title
            editingPlaylist.description = _state.value.description
            playlistInteractor.updatePlaylist(editingPlaylist)

            val id = editingPlaylist.id
            val editingUriImage = imageSaverInteractor.getImage(id)
            val currentUri = _state.value.uri

            if (currentUri != null && currentUri!=editingUriImage) {
                imageSaverInteractor.saveImage(currentUri, id)
            }
            _state.update { it.copy(screenMayBeClosed = true) }
        }
    }

    fun refreshImage(newUri: Uri?) {
        _state.update { it.copy(uri = newUri) }
    }

    fun exitNotConfirmed() {
        _state.update { it.copy(dialogVisibility = false) }
    }

    fun exitConfirmed() {
        _state.update {
            it.copy(dialogVisibility = false, screenMayBeClosed = true)
        }
    }

    fun requestToExit() {
        if( mode == CREATING && _state.value.title.isNotBlank()) {
            _state.update { it.copy(dialogVisibility = true) }
        }
        else
            _state.update { it.copy(screenMayBeClosed = true) }
    }
}
