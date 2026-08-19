package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.panelStyle

@Composable
fun TextField(
    labelText: String,
    onTextChangeAction: () -> Unit,
    modifier: Modifier = Modifier
) {

    var text by rememberSaveable { mutableStateOf("") }
    var borderColor =
        if (text.isNotBlank())
            MaterialTheme.colorScheme.tertiary
        else
            MaterialTheme.colorScheme.onSurfaceVariant

    OutlinedTextField(
        value = text,
        textStyle = panelStyle(),
        onValueChange = {
            text = it
            onTextChangeAction.invoke()
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        label = { Text(labelText) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = borderColor,
            unfocusedLabelColor = borderColor,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

