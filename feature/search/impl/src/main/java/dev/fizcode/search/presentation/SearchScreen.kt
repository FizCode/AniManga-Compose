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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.other.SearchTextFieldComponent
import dev.fizcode.search.presentation.component.SearchResultComponent
import dev.fizcode.search.presentation.model.SearchIntent
import dev.fizcode.search.presentation.model.SearchResultUiModel
import dev.fizcode.search.presentation.model.SearchUiState
import dev.fizcode.search.presentation.model.dummySearchResultUiModel
import dev.fizcode.search.util.Constant
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme

@Composable
internal fun SearchScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    val searchViewModel: SearchViewModel = koinViewModel()
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val queryState = rememberTextFieldState(initialText = uiState.query)

    LaunchedEffect(queryState) {
        snapshotFlow { queryState.text.toString() }
            .distinctUntilChanged()
            .collect { searchViewModel.onIntent(SearchIntent.QueryChanged(it)) }
    }

    SearchScreenContent(
        queryState = queryState,
        uiState = uiState,
        onCardClick = onCardClick
    )
}

@Composable
private fun SearchScreenContent(
    queryState: TextFieldState,
    uiState: SearchUiState,
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
            if (uiState.result is UiState.Loading || uiState.result is UiState.Success) {
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
                result = uiState.result,
                query = uiState.query,
                onCardClick = onCardClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenSuccessPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        SearchScreenContent(
            queryState = rememberTextFieldState(initialText = "Ble"),
            uiState = SearchUiState(
                query = "Ble",
                result = UiState.Success(
                listOf(
                    dummySearchResultUiModel,
                    dummySearchResultUiModel.copy(id = 1, title = "Baybled")
                )
            )
            ),
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenLoadingPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        SearchScreenContent(
            queryState = rememberTextFieldState(initialText = "Ble"),
            uiState = SearchUiState(
                query = "Ble",
                result = UiState.Loading
            ),
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenEmptyPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        SearchScreenContent(
            queryState = rememberTextFieldState(initialText = "Zzz"),
            uiState = SearchUiState(
                query = "Zzz",
                result = UiState.Empty
            ),
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenErrorPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        SearchScreenContent(
            queryState = rememberTextFieldState(initialText = "Ble"),
            uiState = SearchUiState(
                query = "Ble",
                result = UiState.Error(message = "Something went wrong. Please try again.")
            ),
            onCardClick = { _, _ -> }
        )
    }
}
