package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R


@Composable
fun SwitchPanel(
    actionText: String,
    onClickAction: (() -> Unit)? = null )
{
    var checked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(onClickAction!=null) { onClickAction?.invoke()
                                                       checked =!checked },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = actionText,
            style = TextStyles.actionPanelStyle,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
            modifier = Modifier
                .height(18.dp)
                .width(35.dp)
                .padding(end = 53.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.blue),          // цвет «кружка» при checked
                checkedTrackColor = colorResource(id = R.color.blue),      // цвет дорожки при checked
                uncheckedThumbColor = colorResource(id = R.color.grey),        // цвет «кружка» при unchecked
                uncheckedTrackColor = colorResource(id = R.color.yp_light_grey)     // цвет дорожки при unchecked
            )
        )
    }
}

@Preview
@Composable
fun SwitchPanelPreview() {
    SwitchPanel("Темная тема")
}
