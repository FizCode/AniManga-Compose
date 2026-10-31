package dev.fizcode.anime.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.fizcode.anime.presentation.component.AnimeScreenHeader
import dev.fizcode.anime.presentation.component.CurrentSeason
import dev.fizcode.anime.presentation.component.TopAiring
import dev.fizcode.anime.presentation.component.TopRanking
import dev.fizcode.anime.presentation.model.AnimeIntent
import dev.fizcode.anime.presentation.model.AnimeUiState
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.anime.presentation.model.TopAiringUiModel
import dev.fizcode.anime.presentation.model.TopRankingUiModel
import dev.fizcode.anime.presentation.model.dummySeasonalUiModel
import dev.fizcode.anime.presentation.model.dummyTopAiringUiModel
import dev.fizcode.anime.presentation.model.dummyTopRankingUiModel
import dev.fizcode.anime.util.Constant
import dev.fizcode.common.base.callhandler.UiState
import org.koin.androidx.compose.koinViewModel
import dev.fizcode.designsystem.theme.AniMangaAnimeAndMangaInfoTheme

@Composable
internal fun AnimeScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    onSearchClick: () -> Unit,
    onCurrentSeasonClick: () -> Unit,
    onTopAiringClick: () -> Unit,
    onTopRankingClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val animeViewModel: AnimeViewModel = koinViewModel()
    val uiState by animeViewModel.uiState.collectAsStateWithLifecycle()

    AnimeScreenContent(
        uiState = uiState,
        onRefreshClick = { animeViewModel.onIntent(AnimeIntent.Refresh) },
        onCardClick = onCardClick,
        onSearchClick = onSearchClick,
        onCurrentSeasonClick = onCurrentSeasonClick,
        onTopAiringClick = onTopAiringClick,
        onTopRankingClick = onTopRankingClick,
        onSettingsClick = onSettingsClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeScreenContent(
    uiState: AnimeUiState,
    onRefreshClick: () -> Unit = {},
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    onSearchClick: () -> Unit = {},
    onCurrentSeasonClick: () -> Unit = {},
    onTopAiringClick: () -> Unit = {},
    onTopRankingClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimeScreenHeader(
                scrollBehavior = scrollBehavior,
                value = Constant.SEARCH,
                onClickSettings = onSettingsClick,
                onClickSearch = onSearchClick
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefreshClick
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    CurrentSeason(
                        headerTitle = Constant.CURRENT_SEASON,
                        cardItem = uiState.currentSeason,
                        onHeaderClick = onCurrentSeasonClick,
                        onCardClick = onCardClick
                    )
                }
                item {
                    TopAiring(
                        headerTitle = Constant.TOP_AIRING,
                        cardItem = uiState.topAiring,
                        onHeaderClick = onTopAiringClick,
                        onCardClick = onCardClick
                    )
                }
                item {
                    TopRanking(
                        headerTitle = Constant.TOP_RANKING,
                        cardItem = uiState.topRanking,
                        onHeaderClick = onTopRankingClick,
                        onCardClick = onCardClick
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimeScreenPreviewContent(uiState: AnimeUiState) {
    AniMangaAnimeAndMangaInfoTheme {
        AnimeScreenContent(
            uiState = uiState,
            onCardClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimeScreenSuccessPreview() = AnimeScreenPreviewContent(
    AnimeUiState(
        currentSeason = UiState.Success(List(6) { dummySeasonalUiModel }),
        topAiring = UiState.Success(List(5) { dummyTopAiringUiModel }),
        topRanking = UiState.Success(List(5) { dummyTopRankingUiModel })
    )
)

@Preview(showBackground = true)
@Composable
private fun AnimeScreenLoadingPreview() = AnimeScreenPreviewContent(AnimeUiState())

@Preview(showBackground = true)
@Composable
private fun AnimeScreenEmptyPreview() = AnimeScreenPreviewContent(
    AnimeUiState(UiState.Empty, UiState.Empty, UiState.Empty)
)

@Preview(showBackground = true)
@Composable
private fun AnimeScreenErrorPreview() {
    val error = UiState.Error(message = "Something went wrong. Please try again.")
    AnimeScreenPreviewContent(AnimeUiState(error, error, error))
}
