package dev.fizcode.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.other.SearchTextFieldComponent
import dev.fizcode.search.presentation.component.SearchResultComponent
import dev.fizcode.search.presentation.model.SearchResultUiModel
import dev.fizcode.search.presentation.model.dummySearchResultUiModel
import dev.fizcode.search.util.Constant
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val searchResult by searchViewModel.searchResult.collectAsStateWithLifecycle()

    SearchScreenContent(
        queryState = searchViewModel.queryState,
        searchResult = searchResult,
        onCardClick = onCardClick
    )
}

@Composable
private fun SearchScreenContent(
    queryState: TextFieldState,
    searchResult: UiState<List<SearchResultUiModel>>,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SearchTextFieldComponent(
                modifier = Modifier.focusRequester(focusRequester),
                state = queryState,
                placeholder = Constant.PLACEHOLDER
            )
            if (searchResult is UiState.Loading || searchResult is UiState.Success) {
                Text(
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = Constant.SEARCH_RESULT
                )
            } else {
                Spacer(Modifier.height(12.dp))
            }
            SearchResultComponent(
                result = searchResult,
                query = queryState.text.toString().trim(),
                onCardClick = onCardClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreenContent(
        queryState = rememberTextFieldState(initialText = "Ble"),
        searchResult = UiState.Success(
            listOf(
                dummySearchResultUiModel,
                dummySearchResultUiModel.copy(id = 1, title = "Baybled")
            )
        ),
        onCardClick = { _, _ -> }
    )
}
