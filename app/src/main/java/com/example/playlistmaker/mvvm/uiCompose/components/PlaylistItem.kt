package com.example.playlistmaker.mvvm.uiCompose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.media.domain.model.Playlist
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.cardStyle

@Composable
fun PlaylistItem(playlist: Playlist, onClickAction: () -> Unit) {
    val trackCount = playlist.idListTracks.size
    val numbers = pluralStringResource(id = R.plurals.track_count, trackCount, trackCount)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClickAction),
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = playlist.uriImage ?: R.drawable.ic_placeholder_45,
                placeholder = painterResource(R.drawable.ic_placeholder_45)),
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