package com.example.playlistmaker.mvvm.uiCompose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.buttonStyle

@Composable
fun ActionButton(
    text: String,
    onClickAction: (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Button(
        onClick = { onClickAction?.invoke() },
        modifier = modifier
            .height(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text = text,
            style = buttonStyle()
        )
    }
}

@Preview
@Composable
fun ActionButtonPreview() {
    ActionButton(text = "кнопка")
}