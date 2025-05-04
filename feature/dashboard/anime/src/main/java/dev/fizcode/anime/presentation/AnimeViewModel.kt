package dev.fizcode.anime.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.fizcode.anime.domain.usecase.AnimeUseCaseGroup
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.anime.presentation.model.TopAiringUiModel
import dev.fizcode.anime.presentation.model.TopRankingUiModel
import dev.fizcode.anime.util.Constant
import dev.fizcode.common.base.presentationhandler.asStateFlow
import dev.fizcode.common.base.responsehandler.UiState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class AnimeViewModel @Inject constructor(
    private val animeUseCaseGroup: AnimeUseCaseGroup,
    private val seasonalUiMapper: SeasonalAnimeUiMapper,
    private val topAiringUiMapper: TopAiringAnimeUiMapper,
    private val topRankingUiMapper: TopRankingAnimeUiMapper
) : ViewModel() {

    val currentSeason: StateFlow<UiState<List<SeasonalUiModel>>> = asStateFlow(
        domain = { animeUseCaseGroup.fetchSeasonAnimeUseCase(Constant.LIMIT_5) },
        mapper = { data -> seasonalUiMapper.mapToSeasonalAnimeUiModel(data) },
        scope = viewModelScope
    )

    val topAiring: StateFlow<UiState<List<TopAiringUiModel>>> = asStateFlow(
        domain = { animeUseCaseGroup.fetchTopAiringAnimeUseCase(Constant.LIMIT_5) },
        mapper = { data -> topAiringUiMapper.mapToAiringAnimeUiModel(data) },
        scope = viewModelScope
    )

    val topRanking: StateFlow<UiState<List<TopRankingUiModel>>> = asStateFlow(
        domain = { animeUseCaseGroup.fetchTopRankingAnimeUseCase(Constant.LIMIT_10) },
        mapper = { data -> topRankingUiMapper.mapToTopRankingAnimeUiModel(data) },
        scope = viewModelScope
    )

}
