package dev.fizcode.seasonal.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.mal.util.Season
import dev.fizcode.seasonal.presentation.component.SeasonTabs
import dev.fizcode.seasonal.presentation.component.SeasonalHeader
import dev.fizcode.seasonal.presentation.component.SeasonalListComponent
import dev.fizcode.seasonal.presentation.model.SeasonalIntent
import dev.fizcode.seasonal.presentation.model.SeasonalUiModel
import dev.fizcode.seasonal.presentation.model.SeasonalUiState
import dev.fizcode.seasonal.presentation.model.dummySeasonalUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

@Composable
internal fun SeasonalScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    val seasonalViewModel: SeasonalViewModel = koinViewModel()
    val uiState by seasonalViewModel.uiState.collectAsStateWithLifecycle()
    val queryState = rememberTextFieldState(initialText = uiState.query)

    LaunchedEffect(queryState) {
        snapshotFlow { queryState.text.toString() }
            .distinctUntilChanged()
            .collect { seasonalViewModel.onIntent(SeasonalIntent.QueryChanged(it)) }
    }

    SeasonalScreenContent(
        queryState = queryState,
        uiState = uiState,
        onSeasonSelect = { seasonalViewModel.onIntent(SeasonalIntent.SelectSeason(it)) },
        onYearSelect = { seasonalViewModel.onIntent(SeasonalIntent.SelectYear(it)) },
        onCardClick = onCardClick,
        onRetry = { seasonalViewModel.onIntent(SeasonalIntent.Retry) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonalScreenContent(
    queryState: TextFieldState,
    uiState: SeasonalUiState,
    onSeasonSelect: (Season) -> Unit,
    onYearSelect: (Int) -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    onRetry: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Box(modifier = Modifier.padding(end = 12.dp)) {
                        SeasonalHeader(
                            queryState = queryState,
                            year = uiState.year,
                            years = uiState.years,
                            onYearSelect = onYearSelect
                        )
                    }
                }
            )
        },
        // The bottom bar is already accounted for by the app's scaffold, only keep the status bar.
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            SeasonTabs(selected = uiState.season, onSelect = onSeasonSelect)
            Spacer(Modifier.height(8.dp))
            SeasonalListComponent(
                result = uiState.result,
                isFiltering = uiState.query.isNotEmpty(),
                onCardClick = onCardClick,
                onRetry = onRetry
            )
        }
    }
}

@Composable
private fun SeasonalScreenPreviewContent(result: UiState<List<SeasonalUiModel>>) {
    AniMangaAnimeAndMangaInfoTheme {
        SeasonalScreenContent(
            queryState = rememberTextFieldState(),
            uiState = SeasonalUiState(
                season = Season.WINTER,
                year = 2026,
                years = persistentListOf(2027, 2026, 2025),
                seasonal = result
            ),
            onSeasonSelect = {},
            onYearSelect = {},
            onCardClick = { _, _ -> },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SeasonalScreenSuccessPreview() = SeasonalScreenPreviewContent(
    UiState.Success(listOf(dummySeasonalUiModel, dummySeasonalUiModel.copy(id = 1, rank = 2)))
)

@Preview(showBackground = true)
@Composable
private fun SeasonalScreenLoadingPreview() = SeasonalScreenPreviewContent(UiState.Loading)

@Preview(showBackground = true)
@Composable
private fun SeasonalScreenEmptyPreview() = SeasonalScreenPreviewContent(UiState.Empty)

@Preview(showBackground = true)
@Composable
private fun SeasonalScreenErrorPreview() = SeasonalScreenPreviewContent(
    UiState.Error(message = "Something went wrong. Please try again.")
)
