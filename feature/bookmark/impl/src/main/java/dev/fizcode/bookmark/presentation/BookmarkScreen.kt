package dev.fizcode.bookmark.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.bookmark.presentation.component.BookmarkItemComponent
import dev.fizcode.bookmark.presentation.model.BookmarkIntent
import dev.fizcode.bookmark.presentation.model.BookmarkTab
import dev.fizcode.bookmark.presentation.model.BookmarkUiModel
import dev.fizcode.bookmark.presentation.model.BookmarkUiState
import dev.fizcode.bookmark.util.Constant
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.designsystem.component.other.PillTabs
import dev.fizcode.designsystem.component.other.PillTabsShimmer
import dev.fizcode.designsystem.component.other.SearchTextFieldComponent
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme
import dev.fizcode.designsystem.util.DashboardTopBarHeight
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun BookmarkScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    val bookmarkViewModel: BookmarkViewModel = koinViewModel()
    val uiState by bookmarkViewModel.uiState.collectAsStateWithLifecycle()
    val queryState = rememberTextFieldState(initialText = uiState.query)

    LaunchedEffect(queryState) {
        snapshotFlow { queryState.text.toString() }
            .distinctUntilChanged()
            .collect { bookmarkViewModel.onIntent(BookmarkIntent.QueryChanged(it)) }
    }

    BookmarkScreenContent(
        queryState = queryState,
        uiState = uiState,
        onTabSelect = { bookmarkViewModel.onIntent(BookmarkIntent.SelectTab(it)) },
        onCardClick = onCardClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkScreenContent(
    queryState: TextFieldState,
    uiState: BookmarkUiState,
    onTabSelect: (BookmarkTab) -> Unit,
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DashboardTopBarHeight)
                            .padding(end = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        SearchTextFieldComponent(
                            state = queryState,
                            placeholder = Constant.PLACEHOLDER
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
            if (uiState.bookmarks is UiState.Loading) {
                PillTabsShimmer(labels = BookmarkTab.entries.map { it.label })
            } else {
                PillTabs(
                    items = BookmarkTab.entries,
                    selected = uiState.tab,
                    label = { it.label },
                    onSelect = onTabSelect
                )
            }
            Spacer(Modifier.height(8.dp))
            BookmarkItemComponent(
                cardItem = uiState.bookmarks,
                emptyMessage = if (uiState.query.isNotEmpty()) {
                    Constant.EMPTY_QUERY_RESULT
                } else {
                    Constant.EMPTY_BOOKMARK_FORMAT.format(uiState.tab.emptyLabel)
                },
                onCardClick = onCardClick
            )
        }
    }
}

private val previewBookmark = BookmarkUiModel(
    mediaId = 0,
    mediaType = "TV",
    posterPath = "",
    rating = "5.00",
    title = "BLEACH: Sennen Kessen-hen",
    subTitle = "TV | Eps. 10 of 12 in 6d 2h",
    studio = "Toei Animation",
    genre = persistentListOf("Action", "Adventure", "Comedy")
)

@Composable
private fun BookmarkScreenPreviewContent(
    bookmarks: UiState<List<BookmarkUiModel>>,
    tab: BookmarkTab = BookmarkTab.ANIME
) {
    AniMangaAnimeAndMangaInfoTheme {
        BookmarkScreenContent(
            queryState = rememberTextFieldState(),
            uiState = BookmarkUiState(tab = tab, allBookmarks = bookmarks),
            onTabSelect = {},
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BookmarkScreenSuccessPreview() = BookmarkScreenPreviewContent(
    UiState.Success(List(3) { previewBookmark.copy(mediaId = it) })
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BookmarkScreenLoadingPreview() = BookmarkScreenPreviewContent(UiState.Loading)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BookmarkScreenEmptyPreview() = BookmarkScreenPreviewContent(UiState.Empty)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BookmarkScreenEmptyMangaPreview() =
    BookmarkScreenPreviewContent(UiState.Empty, tab = BookmarkTab.MANGA)
