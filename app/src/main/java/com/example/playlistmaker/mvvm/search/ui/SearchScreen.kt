package com.example.playlistmaker.mvvm.search.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
//import androidx.compose.material3.SearchBar
//import androidx.compose.material3.SearchBarDefaults
//import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.ComposeTheme
import com.example.playlistmaker.mvvm.uiCompose.Header
import com.example.playlistmaker.mvvm.uiCompose.TextStyles
import com.example.playlistmaker.mvvm.uiCompose.TextStyles.panelStyle
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(viewModel: SearchViewModel, modifier: Modifier = Modifier) {

    // val state by viewModel.uiState.collectAsState() // LiveData/StateFlow → State

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Header(stringResource(R.string.search))

        SearchField(isSystemInDarkTheme(), { Log.d("mylog","текст изменился")})

    }
}


@Composable
fun SearchField(theme: Boolean, onTextChangeAction: (() -> Unit) ) {
    ComposeTheme(theme) {

        var text by rememberSaveable {
            mutableStateOf("")
        }
        Box(
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it
                    onTextChangeAction.invoke()},
                singleLine = true,
                textStyle = panelStyle(),
                modifier = Modifier
                    .height(36.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    ),
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
                                    .clickable { text = "" }
                            )
                        }
                    }
                }
            )
        }
    }
}

