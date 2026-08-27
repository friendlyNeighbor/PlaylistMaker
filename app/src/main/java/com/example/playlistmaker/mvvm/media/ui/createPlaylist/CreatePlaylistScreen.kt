package com.example.playlistmaker.mvvm.media.ui.createPlaylist

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.components.AdditionalButton
import com.example.playlistmaker.mvvm.uiCompose.components.Dialog
import com.example.playlistmaker.mvvm.uiCompose.components.Header
import com.example.playlistmaker.mvvm.uiCompose.components.TextField
import com.example.playlistmaker.mvvm.uiCompose.dashedBorder
import org.koin.androidx.compose.koinViewModel


@Composable
fun CreatePlaylistScreen(
    navController: NavController,
    viewModel: CreatePlaylistViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsState()

    val imageUri = uiState.uri
    val textTitle = uiState.title
    val textDescription = uiState.description
    val modeEditing = uiState.modeEditing
    val dialogIsVisible = uiState.dialogVisibility

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.refreshImage(uri)
            }
        }

    val textHeader =
        if (modeEditing)
            stringResource(R.string.edit_playlist)
        else
            stringResource(R.string.new_playlist)

    val textButton =
        if (modeEditing)
            stringResource(R.string.complete)
        else
            stringResource(R.string.create)

    Box(modifier = Modifier
        .fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Header(
                textHeader,
                R.drawable.ic_arrow_back_24,
                onClickAction = { viewModel.requestToExit() }
            )

            Image(
                modifier = Modifier
                    .padding(top = 26.dp, start = 24.dp, end = 24.dp)
                    .aspectRatio(1f)
                    .weight(1f, fill = false)
                    .clip(RoundedCornerShape(8.dp))
                    .dashedBorder(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        strokeWidth = 1.dp,
                        cornerRadius = 8.dp,
                        dashLength = 30.dp,
                        gapLength = 30.dp
                    )
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentScale = if (imageUri != null) {
                    ContentScale.Crop
                } else {
                    ContentScale.None
                },
                painter = rememberAsyncImagePainter(
                    model = imageUri ?: R.drawable.ic_cover,
                    placeholder = painterResource(R.drawable.ic_cover)
                ),
                contentDescription = null
            )

            TextField(
                value = textTitle,
                onValueChange = { newText -> viewModel.onTextTitleChange(newText) },
                labelText = stringResource(R.string.title),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 33.dp)
            )

            TextField(
                value = textDescription,
                onValueChange = { newText -> viewModel.onTextDescriptionChange(newText) },
                labelText = stringResource(R.string.description),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )

            Spacer(modifier = Modifier.height(92.dp))
        }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        val additionalButtonIsEnabled = textTitle.isNotBlank()

        AdditionalButton(
            text = textButton,
            onClickAction = { viewModel.savePlaylist() },
            isEnabled = additionalButtonIsEnabled,
            modifier = Modifier
                .padding(start = 17.dp, end = 17.dp, bottom = 32.dp, top = 16.dp)
        )
    }

        Dialog(
            visible = dialogIsVisible,
            textTitle = stringResource(R.string.finish_creating),
            text = stringResource(R.string.data_will_be_lost),
            textConfirmButton = stringResource(R.string.complete),
            textDismissButton = stringResource(R.string.cancel),
            onDismissRequest = { viewModel.exitNotConfirmed() },
            onConfirmation = { viewModel.exitConfirmed() },
            onDismiss = { viewModel.exitNotConfirmed() }
        )
    }

    BackHandler { viewModel.requestToExit() }

    if (uiState.screenMayBeClosed)
        navController.navigateUp()

    if (uiState.savingComplete) {
        val context = LocalContext.current
        Toast.makeText(
            context,
            stringResource(R.string.playlist_created, textTitle),
            Toast.LENGTH_SHORT
        ).show()
        navController.navigateUp()
    }

}
