package com.example.playlistmaker.mvvm.uiCompose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.mvvm.uiCompose.TextStyles


@Composable
fun Header(text: String, imageRes: Int? = null, onClickAction: (() -> Unit)?=null, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClickAction!=null) { onClickAction?.invoke() },
        verticalAlignment = Alignment.CenterVertically

    ) {
        if(imageRes!=null) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
        Text(
            text = text,
            style = TextStyles.headerStyle(),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
fun HeaderPreview() {
    Header("Заголовок")
}