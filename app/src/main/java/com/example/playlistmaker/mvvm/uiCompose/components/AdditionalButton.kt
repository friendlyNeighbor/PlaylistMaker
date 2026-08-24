package com.example.playlistmaker.mvvm.uiCompose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.additionalButtonStyle

@Composable
fun AdditionalButton(
    text: String,
    onClickAction: (() -> Unit)? = null,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClickAction?.invoke() },
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimaryFixed,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryFixed,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryFixed
                ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            style = additionalButtonStyle()
        )
    }
}
