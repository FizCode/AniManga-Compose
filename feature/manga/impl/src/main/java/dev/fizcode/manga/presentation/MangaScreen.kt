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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.other.PillTabs
import dev.fizcode.manga.presentation.component.MangaHeader
import dev.fizcode.manga.presentation.component.MangaListComponent
import dev.fizcode.manga.presentation.model.MangaSort
import dev.fizcode.manga.presentation.model.MangaType
import dev.fizcode.manga.presentation.model.MangaUiModel
import dev.fizcode.manga.presentation.model.dummyMangaUiModel
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
    val result by mangaViewModel.mangaResult.collectAsStateWithLifecycle()
    val type by mangaViewModel.selectedType.collectAsStateWithLifecycle()
    val sort by mangaViewModel.selectedSort.collectAsStateWithLifecycle()

    MangaScreenContent(
        queryState = mangaViewModel.queryState,
        type = type,
        sort = sort,
        result = result,
        onTypeSelect = mangaViewModel::selectType,
        onSortSelect = mangaViewModel::selectSort,
        onRetry = mangaViewModel::retry,
        onCardClick = onCardClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MangaScreenContent(
    queryState: TextFieldState,
    type: MangaType,
    sort: MangaSort,
    result: UiState<List<MangaUiModel>>,
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
                            sort = sort,
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
                selected = type,
                label = { it.label },
                onSelect = onTypeSelect
            )
            Spacer(Modifier.height(8.dp))
            MangaListComponent(
                result = result,
                isFiltering = queryState.text.isNotBlank(),
                onRetry = onRetry,
                onCardClick = onCardClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaScreenSuccessPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaScreenContent(
            queryState = rememberTextFieldState(),
            type = MangaType.ALL,
            sort = MangaSort.RANKING,
            result = UiState.Success(listOf(dummyMangaUiModel, dummyMangaUiModel.copy(id = 1))),
            onTypeSelect = {},
            onSortSelect = {},
            onRetry = {},
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaScreenLoadingPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaScreenContent(
            queryState = rememberTextFieldState(),
            type = MangaType.ALL,
            sort = MangaSort.RANKING,
            result = UiState.Loading,
            onTypeSelect = {},
            onSortSelect = {},
            onRetry = {},
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaScreenEmptyPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaScreenContent(
            queryState = rememberTextFieldState(),
            type = MangaType.ALL,
            sort = MangaSort.RANKING,
            result = UiState.Empty,
            onTypeSelect = {},
            onSortSelect = {},
            onRetry = {},
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MangaScreenErrorPreview() {
    AniMangaAnimeAndMangaInfoTheme {
        MangaScreenContent(
            queryState = rememberTextFieldState(),
            type = MangaType.ALL,
            sort = MangaSort.RANKING,
            result = UiState.ErrorException(message = "Something went wrong. Please try again."),
            onTypeSelect = {},
            onSortSelect = {},
            onRetry = {},
            onCardClick = { _, _ -> }
        )
    }
}
