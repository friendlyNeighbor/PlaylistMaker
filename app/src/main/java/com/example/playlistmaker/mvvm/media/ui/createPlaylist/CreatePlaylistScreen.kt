package com.example.playlistmaker.mvvm.media.ui.createPlaylist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.AdditionalButton
import com.example.playlistmaker.mvvm.uiCompose.Header
import com.example.playlistmaker.mvvm.uiCompose.TextField
import com.example.playlistmaker.mvvm.uiCompose.dashedBorder
import org.koin.androidx.compose.koinViewModel




@Composable
fun CreatePlaylistScreen(
    navController: NavController,
    playlistId:Long?
) {
    val viewModel: CreatePlaylistViewModel = koinViewModel()
    val uiState by viewModel.getLiveData().observeAsState()

    val EDITING = "EDITING"
    val CREATING = "CREATING"

    var state = CREATING

    if(playlistId!=null) {
        viewModel.loadPlaylistById(playlistId)
        state = EDITING
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        val imageUri = uiState?.uri
        var textTitle by rememberSaveable { mutableStateOf("") }

        Header(
            stringResource(R.string.new_playlist),
            R.drawable.ic_arrow_back_24,
            onClickAction = { empty() }

        )

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(top = 26.dp, start = 24.dp, end = 24.dp)
                .clip(RoundedCornerShape(8.dp))
                .dashedBorder(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    strokeWidth = 1.dp,
                    cornerRadius = 8.dp,
                    dashLength = 30.dp,
                    gapLength = 30.dp
                ),
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
            labelText = stringResource(R.string.title),
            { textTitle = " 2 " },      //fdddddddddddddg
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 33.dp)
        )
        TextField(
            labelText = stringResource(R.string.description),
            {},
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        val additionalButtonIsEnabled = textTitle.isNotBlank()

        AdditionalButton(
            text = stringResource(R.string.create),
            isEnabled = additionalButtonIsEnabled,
            modifier = Modifier
                .padding(start = 17.dp, end = 17.dp, bottom = 32.dp)
        )

    }


   // requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
   //     closeFragment()
   fun empty() {}

    fun closeFragment() {
        if(state == EDITING)
            navController.navigateUp()
        else {
            if (
                viewModel.uriImage == null// &&
            //       binding.titlePlaylist.text.toString().isEmpty() &&
            //       binding.description.text.toString().isEmpty()
            )
                navController.navigateUp()
            else {
                //  showDialog()
            }
        }
    }

    fun showDialog() {
        /*
        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.finish_creating)
            .setMessage(R.string.data_will_be_lost)
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.complete) { _, _ ->
                navController().navigateUp()
            }
        confirmDialog.show()


         */
    }



}

fun empty() {}
