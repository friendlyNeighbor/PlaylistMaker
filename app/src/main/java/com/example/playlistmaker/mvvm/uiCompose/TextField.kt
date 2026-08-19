package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.panelStyle

@Composable
fun TextField(placeHolderText:String, onTextChangeAction: () -> Unit ,modifier: Modifier = Modifier) {

    var text by rememberSaveable {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text,
        textStyle = panelStyle(),
        onValueChange = {
            text = it
            onTextChangeAction.invoke() },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(
                text = placeHolderText,
                style = panelStyle(),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,

            focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,

            focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,

            cursorColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

