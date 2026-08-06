package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R

@Composable
fun ActionPanel(
    actionText: String, iconRes: Int?,
    onClickAction: (() -> Unit)? = null ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(onClickAction!=null) { onClickAction?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = actionText,
            style = TextStyles.actionPanelStyle,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        )

        if(iconRes!=null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .height(61.dp)
                    .padding(end = 12.dp)
            )
        }
    }
}

@Preview
@Composable
fun ActionPanelPreview() {
    ActionPanel("Действие", R.drawable.ic_share_24)
}