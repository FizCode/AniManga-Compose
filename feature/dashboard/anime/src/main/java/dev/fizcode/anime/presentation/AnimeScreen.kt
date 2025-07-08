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
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.anime.presentation.model.TopAiringUiModel
import dev.fizcode.anime.presentation.model.TopRankingUiModel
import dev.fizcode.anime.presentation.model.dummySeasonalUiModel
import dev.fizcode.anime.presentation.model.dummyTopAiringUiModel
import dev.fizcode.anime.presentation.model.dummyTopRankingUiModel
import dev.fizcode.anime.util.Constant
import dev.fizcode.common.base.responsehandler.UiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnimeScreen(
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    animeViewModel: AnimeViewModel = koinViewModel()
) {

    val currentSeason by animeViewModel.currentSeason.collectAsStateWithLifecycle()
    val topAiring by animeViewModel.topAiring.collectAsStateWithLifecycle()
    val topRanking by animeViewModel.topRanking.collectAsStateWithLifecycle()
    val refreshing by animeViewModel.refreshing.collectAsStateWithLifecycle()

    val isRefreshing = refreshing && (
            currentSeason is UiState.Loading ||
                    topAiring is UiState.Loading ||
                    topRanking is UiState.Loading
            )

    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            animeViewModel.shouldNotRefresh()
        }
    }

    AnimeScreenContent(
        currentSeason = currentSeason,
        topAiring = topAiring,
        topRanking = topRanking,
        isRefreshing = isRefreshing,
        onRefreshClick = animeViewModel::refresh,
        onCardClick = onCardClick
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeScreenContent(
    currentSeason: UiState<List<SeasonalUiModel>>,
    topAiring: UiState<List<TopAiringUiModel>>,
    topRanking: UiState<List<TopRankingUiModel>>,
    isRefreshing: Boolean = false,
    onRefreshClick: () -> Unit = {},
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
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
                value = Constant.AnimeSearchHeader.SEARCH,
                onClickSettings = {},
                onClickSearch = {}
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            isRefreshing = isRefreshing,
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
                        cardItem = currentSeason,
                        onHeaderClick = {},
                        onCardClick = onCardClick
                    )
                }
                item {
                    TopAiring(
                        headerTitle = Constant.TOP_AIRING,
                        cardItem = topAiring,
                        onHeaderClick = {},
                        onCardClick = onCardClick
                    )
                }
                item {
                    TopRanking(
                        headerTitle = Constant.TOP_RANKING,
                        cardItem = topRanking,
                        onHeaderClick = {},
                        onCardClick = onCardClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun AnimeScreenPreview() {
    AnimeScreenContent(
        currentSeason = UiState.Success(
            listOf(
                dummySeasonalUiModel,
                dummySeasonalUiModel,
                dummySeasonalUiModel,
                dummySeasonalUiModel,
                dummySeasonalUiModel,
                dummySeasonalUiModel
            )
        ),
        topAiring = UiState.Success(
            listOf(
                dummyTopAiringUiModel,
                dummyTopAiringUiModel,
                dummyTopAiringUiModel,
                dummyTopAiringUiModel,
                dummyTopAiringUiModel
            )
        ),
        topRanking = UiState.Success(
            listOf(
                dummyTopRankingUiModel,
                dummyTopRankingUiModel,
                dummyTopRankingUiModel,
                dummyTopRankingUiModel,
                dummyTopRankingUiModel
            )
        ),
        onCardClick = { _, _ -> }
    )
}
