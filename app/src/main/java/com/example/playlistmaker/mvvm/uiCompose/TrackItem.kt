package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import coil.compose.rememberAsyncImagePainter
import com.example.playlistmaker.mvvm.search.domain.model.Track
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.panelStyle
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.panelStyleSmall


@Composable
fun TrackItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
            ) {

        Image(
            painter = rememberAsyncImagePainter(model = track.artworkUrl100),
            //painter = painterResource(R.drawable.ic_placeholder_45),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 13.dp, end = 8.dp)
                .size(45.dp, 45.dp),
            contentScale = ContentScale.Crop

        )

        Column(
            modifier = Modifier
            .weight(1f)
            .padding(vertical = 14.dp)) {

            Text(
                text = track.trackName,
                style = panelStyle(),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.height(19.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = track.artistName,
                    style = panelStyleSmall(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.height(13.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_dot_13),
                    contentDescription = null,
                    modifier = Modifier
                        .size(13.dp)    // нужно?
                )

                Text(
                    text = track.trackTime,
                    style = panelStyleSmall(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.height(13.dp)
                    )
            }
        }

        Image(
            painter = painterResource(R.drawable.ic_arrow_forward_24),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp, end = 12.dp)
        )

    }
}

val track:Track = Track(
    0,
1,
"Группа крови",
"Кино",
"03:45",
"https://img.goodfon.ru/wallpaper/nbig/c/c9/enot-vzgliad-voda-pogruzhenie-morda.webp",
"2026",
"2026",
"Рок",
"Россия",
"нет"
)


@Preview
@Composable
fun TrackItemLight() {
    ComposeTheme(true) {
            TrackItem(track)
    }}

@Preview
@Composable
fun TrackItemDark() {
    ComposeTheme(false) {
        TrackItem(track)
    }}
