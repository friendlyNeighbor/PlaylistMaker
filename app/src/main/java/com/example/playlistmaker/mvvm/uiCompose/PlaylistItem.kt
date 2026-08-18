package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.cardStyle

@Composable
fun PlaylistItem(playlist: Playlist, onClickAction: () -> Unit) {
    val uriImage = if(playlist.uriImage!=null) playlist.uriImage else (R.drawable.ic_placeholder_45)
    val trackCount = playlist.idListTracks.size
    val numbers = pluralStringResource(id = R.plurals.track_count, trackCount, trackCount)
    Column(
        modifier = Modifier
            .fillMaxWidth()
  //          .height(61.dp)
  //          .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClickAction),
  //      verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = uriImage),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = playlist.title,
            style = cardStyle(),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .height(16.dp)
                .padding(top = 4.dp)
        )
        Text(
            text = numbers,
            style = cardStyle(),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .height(16.dp)
        )
    }
}