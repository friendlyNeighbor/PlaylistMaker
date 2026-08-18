package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextField() {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text("Название")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,

            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,

            focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,

            cursorColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun TextFieldPreview() {
    TextField()
}