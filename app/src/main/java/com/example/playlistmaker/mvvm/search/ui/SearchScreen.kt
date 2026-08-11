package com.example.playlistmaker.mvvm.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.SearchBar
//import androidx.compose.material3.SearchBarDefaults
//import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.uiCompose.Header
import com.example.playlistmaker.mvvm.uiCompose.TextStyles
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(viewModel: SearchViewModel, modifier: Modifier = Modifier ) {

    // val state by viewModel.uiState.collectAsState() // LiveData/StateFlow → State

    Column(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Header(stringResource(R.string.search))

        SearchField(
            "Поиск",
            {},
            {}
        )

    }
}



@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text(text = stringResource(R.string.search)) },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search_16),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") } // <-- очистка по клику
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_clear_16),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        textStyle = TextStyles.searchPanelStyle()
    )
}