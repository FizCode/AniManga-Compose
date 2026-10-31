package dev.fizcode.manga.presentation

import androidx.compose.foundation.layout.Arrangement
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
import dev.fizcode.designsystem.component.other.PillTabs
import dev.fizcode.manga.presentation.component.MangaHeader
import dev.fizcode.manga.presentation.component.MangaListComponent
import dev.fizcode.manga.presentation.model.MangaIntent
import dev.fizcode.manga.presentation.model.MangaRankingRequest
import dev.fizcode.manga.presentation.model.MangaSort
import dev.fizcode.manga.presentation.model.MangaType
import dev.fizcode.manga.presentation.model.MangaUiModel
import dev.fizcode.manga.presentation.model.MangaUiState
import dev.fizcode.manga.presentation.model.RankedManga
import dev.fizcode.manga.presentation.model.dummyMangaUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

@Composable
internal fun MangaScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    val mangaViewModel: MangaViewModel = koinViewModel()
    val uiState by mangaViewModel.uiState.collectAsStateWithLifecycle()
    val queryState = rememberTextFieldState(initialText = uiState.query)

    LaunchedEffect(queryState) {
        snapshotFlow { queryState.text.toString() }
            .distinctUntilChanged()
            .collect { mangaViewModel.onIntent(MangaIntent.QueryChanged(it)) }
    }

    MangaScreenContent(
        queryState = queryState,
        uiState = uiState,
        onTypeSelect = { mangaViewModel.onIntent(MangaIntent.SelectType(it)) },
        onSortSelect = { mangaViewModel.onIntent(MangaIntent.SelectSort(it)) },
        onRetry = { mangaViewModel.onIntent(MangaIntent.Retry) },
        onCardClick = onCardClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MangaScreenContent(
    queryState: TextFieldState,
    uiState: MangaUiState,
    onTypeSelect: (MangaType) -> Unit,
    onSortSelect: (MangaSort) -> Unit,
    onRetry: () -> Unit,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
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
                        MangaHeader(
                            queryState = queryState,
                            sort = uiState.sort,
                            onSortSelect = onSortSelect
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
            PillTabs(
                items = MangaType.entries,
                selected = uiState.type,
                label = { it.label },
                onSelect = onTypeSelect
            )
            Spacer(Modifier.height(8.dp))
            MangaListComponent(
                result = uiState.result,
                isFiltering = uiState.query.isNotEmpty(),
                onRetry = onRetry,
                onCardClick = onCardClick
            )
        }
    }
}

@Composable
private fun MangaScreenPreviewContent(result: UiState<List<MangaUiModel>>) {
    AniMangaAnimeAndMangaInfoTheme {
        MangaScreenContent(
            queryState = rememberTextFieldState(),
            uiState = MangaUiState(ranked = RankedManga(MangaRankingRequest.of(MangaType.ALL, MangaSort.RANKING), result)),
            onTypeSelect = {},
            onSortSelect = {},
            onRetry = {},
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaScreenSuccessPreview() = MangaScreenPreviewContent(
    UiState.Success(listOf(dummyMangaUiModel, dummyMangaUiModel.copy(id = 1)))
)

@Preview(showBackground = true)
@Composable
private fun MangaScreenLoadingPreview() = MangaScreenPreviewContent(UiState.Loading)

@Preview(showBackground = true)
@Composable
private fun MangaScreenEmptyPreview() = MangaScreenPreviewContent(UiState.Empty)

@Preview(showBackground = true)
@Composable
private fun MangaScreenErrorPreview() = MangaScreenPreviewContent(
    UiState.Error(message = "Something went wrong. Please try again.")
)
