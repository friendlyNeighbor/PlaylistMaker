package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.placeholderStyle

@Composable
fun PlaceHolder(imageId: Int, stringRes: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .width(312.dp),
            textAlign = TextAlign.Center,
            text = stringResource(stringRes),
            style = placeholderStyle(),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}