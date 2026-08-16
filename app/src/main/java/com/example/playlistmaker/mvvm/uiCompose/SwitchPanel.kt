package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R


@Composable
fun SwitchPanel(
    actionText: String,
    onClickAction: (() -> Unit)? = null,
    switchChecked: Boolean = false)
{
    var checked by remember { mutableStateOf(switchChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(onClickAction!=null) {  checked =!checked
                                                        onClickAction?.invoke()}
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = actionText,
            style = TextStyles.panelStyle(),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                onClickAction?.invoke()
            },
            modifier = Modifier
                .padding(end = 18.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.tertiary,          // цвет «кружка» при checked
                checkedTrackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.45f),      // цвет дорожки при checked
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,        // цвет «кружка» при unchecked
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary     // цвет дорожки при unchecked
            )
        )
    }
}

@Preview
@Composable
fun SwitchPanelPreview() {
    SwitchPanel("Темная тема")
}
