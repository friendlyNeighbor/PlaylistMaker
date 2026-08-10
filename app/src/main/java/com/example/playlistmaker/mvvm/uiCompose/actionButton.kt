package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.actionButtonStyle

@Composable
fun ActionButton(text: String, onClickAction: (() -> Unit)? = null) {

    Button(
        onClick = { onClickAction?.invoke() },
        modifier = Modifier.height(36.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(18.dp)
) {
        Text(
            text= text,
            style = actionButtonStyle()
        )
    }

}

@Preview
@Composable
fun ActionButtonPreview() {
    ActionButton( "кнопка")
}