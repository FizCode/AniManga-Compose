package dev.fizcode.anime.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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

@Composable
internal fun AnimeScreen(
    innerPadding: PaddingValues,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit,
    animeViewModel: AnimeViewModel = hiltViewModel()
) {

    val currentSeason by animeViewModel.currentSeason.collectAsStateWithLifecycle()
    val topAiring by animeViewModel.topAiring.collectAsStateWithLifecycle()
    val topRanking by animeViewModel.topRanking.collectAsStateWithLifecycle()

    AnimeScreenContent(
        innerPadding = innerPadding,
        currentSeason = currentSeason,
        topAiring = topAiring,
        topRanking = topRanking,
        onCardClick = onCardClick
    )

}

@Composable
private fun AnimeScreenContent(
    innerPadding: PaddingValues = PaddingValues.Absolute(),
    currentSeason: UiState<List<SeasonalUiModel>>,
    topAiring: UiState<List<TopAiringUiModel>>,
    topRanking: UiState<List<TopRankingUiModel>>,
    onCardClick: (mediaType: String, mediaId: Int) -> Unit
) {
    Column(Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(innerPadding)) {
        Spacer(modifier = Modifier.height(16.dp))
        AnimeScreenHeader(
            value = Constant.AnimeSearchHeader.SEARCH,
            onValueChanged = { _ -> },
            onClickSettings = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        CurrentSeason(
            headerTitle = Constant.CURRENT_SEASON,
            cardItem = currentSeason,
            onHeaderClick = {},
            onCardClick = onCardClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        TopAiring(
            headerTitle = Constant.TOP_AIRING,
            cardItem = topAiring,
            onHeaderClick = {},
            onCardClick = onCardClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        TopRanking(
            headerTitle = Constant.TOP_RANKING,
            cardItem = topRanking,
            onHeaderClick = {},
            onCardClick = onCardClick
        )
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun AnimeScreenPreview() {
    AnimeScreenContent(
        currentSeason = UiState.Success(listOf(
            dummySeasonalUiModel,
            dummySeasonalUiModel,
            dummySeasonalUiModel,
            dummySeasonalUiModel,
            dummySeasonalUiModel,
            dummySeasonalUiModel
        )),
        topAiring = UiState.Success(listOf(
            dummyTopAiringUiModel,
            dummyTopAiringUiModel,
            dummyTopAiringUiModel,
            dummyTopAiringUiModel,
            dummyTopAiringUiModel
        )),
        topRanking = UiState.Success(listOf(
            dummyTopRankingUiModel,
            dummyTopRankingUiModel,
            dummyTopRankingUiModel,
            dummyTopRankingUiModel,
            dummyTopRankingUiModel
        )),
        onCardClick = { _, _ -> }
    )
}
