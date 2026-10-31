package dev.fizcode.ranking.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.ranking.presentation.component.RankingListComponent
import dev.fizcode.ranking.presentation.component.RankingTopBar
import dev.fizcode.ranking.presentation.model.RankingIntent
import dev.fizcode.ranking.presentation.model.RankingType
import dev.fizcode.ranking.presentation.model.RankingUiModel
import dev.fizcode.ranking.presentation.model.RankingUiState
import dev.fizcode.ranking.presentation.model.dummyRankingUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/** The full list of one [RankingType], with a title search that opens in the top bar. */
@Composable
internal fun RankingScreen(
    type: RankingType,
    onBackClick: () -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    val rankingViewModel: RankingViewModel = koinViewModel(key = type.name) {
        parametersOf(type.apiValue)
    }
    val uiState by rankingViewModel.uiState.collectAsStateWithLifecycle()
    val queryState = rememberTextFieldState(initialText = uiState.query)

    LaunchedEffect(queryState) {
        snapshotFlow { queryState.text.toString() }
            .distinctUntilChanged()
            .collect { rankingViewModel.onIntent(RankingIntent.QueryChanged(it)) }
    }

    RankingScreenContent(
        title = type.title,
        queryState = queryState,
        uiState = uiState,
        onBackClick = onBackClick,
        onCardClick = onCardClick,
        onRetry = { rankingViewModel.onIntent(RankingIntent.Retry) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RankingScreenContent(
    title: String,
    queryState: TextFieldState,
    uiState: RankingUiState,
    onBackClick: () -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    onRetry: () -> Unit
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // The first back press only closes the search, the next one leaves the screen.
    BackHandler(enabled = isSearching) {
        isSearching = false
        queryState.clearText()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            RankingTopBar(
                title = title,
                isSearching = isSearching,
                queryState = queryState,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick,
                onSearchClick = { isSearching = true }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            RankingListComponent(
                result = uiState.result,
                query = uiState.query,
                onCardClick = onCardClick,
                onRetry = onRetry
            )
        }
    }
}

@Composable
private fun RankingScreenPreviewContent(
    result: UiState<List<RankingUiModel>>,
    query: String = ""
) {
    AniMangaAnimeAndMangaInfoTheme {
        RankingScreenContent(
            title = RankingType.TOP_AIRING.title,
            queryState = rememberTextFieldState(initialText = query),
            uiState = RankingUiState(query = query, ranking = result),
            onBackClick = {},
            onCardClick = { _, _ -> },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RankingScreenSuccessPreview() = RankingScreenPreviewContent(
    UiState.Success(List(3) { dummyRankingUiModel.copy(id = it, rank = it + 1) })
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RankingScreenLoadingPreview() = RankingScreenPreviewContent(UiState.Loading)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RankingScreenEmptyPreview() = RankingScreenPreviewContent(UiState.Empty)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RankingScreenErrorPreview() = RankingScreenPreviewContent(
    UiState.Error(message = "Something went wrong. Please try again.")
)
