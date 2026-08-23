package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.buttonStyle
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.panelStyle

@Composable
fun Dialog(
    visible: Boolean,
    textTitle: String,
    text: String,
    textConfirmButton: String,
    textDismissButton: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit
    ) {
    if (visible) {
        AlertDialog(
            title = { Text(
                text = textTitle,
                style = panelStyle(),
                color = MaterialTheme.colorScheme.onPrimary
            ) },

            text = { Text(
                text = text,
                style = buttonStyle(),
                color = MaterialTheme.colorScheme.onPrimary
            ) },

            onDismissRequest = {
                onDismissRequest()
            },

            confirmButton = {
                Button(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(
                        text = textConfirmButton,
                        style = panelStyle(),
                        color = MaterialTheme.colorScheme.tertiary
                        )
                }
            },

            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(
                        text = textDismissButton,
                        style = panelStyle(),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            },

            containerColor = MaterialTheme.colorScheme.primary
        )
    }
}