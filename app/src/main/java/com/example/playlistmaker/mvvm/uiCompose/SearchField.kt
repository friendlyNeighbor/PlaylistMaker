package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.panelStyle
import android.util.Log

@Composable
fun SearchField(theme: Boolean, onTextChangeAction: (String) -> Unit, onFocusedAction: ((Boolean) -> Unit)?=null ) {
    ComposeTheme(theme) {

        var text by rememberSaveable {
            mutableStateOf("")
        }
     //   var isFocused by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it
                    onTextChangeAction(text)},
                singleLine = true,
                textStyle = panelStyle(),
                modifier = Modifier
                    .height(36.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .onFocusChanged { focusState ->
                        Log.d("mylog", "onFocus Change")
                 //       isFocused = focusState.isFocused
                        if (focusState.isFocused && text=="") {
                            Log.d("mylog", "focusState.isFocused")
                            onFocusedAction?.invoke(focusState.isFocused)
                        }
                        else
                            Log.d("mylog", "focusState do not Focused")
                        onFocusedAction?.invoke(focusState.isFocused)
                    },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search_16),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(
                                    start = 12.dp,
                                    end = 10.dp
                                )
                                .size(16.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart,

                            ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "Поиск",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = panelStyle()
                                )
                            }
                            innerTextField()
                        }
                        if (text.isNotEmpty()) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_clear_16),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(
                                        start = 10.dp,
                                        end = 12.dp
                                    )
                                    .size(16.dp)
                                    .clickable { text = ""
                                                 onTextChangeAction(text)}
                            )
                        }
                    }
                }
            )
        }
    }
}
