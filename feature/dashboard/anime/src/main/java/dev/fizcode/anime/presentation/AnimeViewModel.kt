package dev.fizcode.anime.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.fizcode.anime.domain.repository.AnimeRepository
import dev.fizcode.anime.presentation.mapper.SeasonalAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopAiringAnimeUiMapper
import dev.fizcode.anime.presentation.mapper.TopRankingAnimeUiMapper
import dev.fizcode.anime.presentation.model.SeasonalUiModel
import dev.fizcode.anime.presentation.model.TopAiringUiModel
import dev.fizcode.anime.presentation.model.TopRankingUiModel
import dev.fizcode.common.base.presentationhandler.asStateFlow
import dev.fizcode.common.base.responsehandler.UiState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val seasonalUiMapper: SeasonalAnimeUiMapper,
    private val topAiringUiMapper: TopAiringAnimeUiMapper,
    private val topRankingUiMapper: TopRankingAnimeUiMapper
) : ViewModel() {

    val currentSeason: StateFlow<UiState<List<SeasonalUiModel>>> = asStateFlow(
        domain = { repository.fetchSeasonAnime() },
        mapper = { data -> seasonalUiMapper.mapToSeasonalAnimeUiModel(data) },
        scope = viewModelScope
    )

    val topAiring: StateFlow<UiState<List<TopAiringUiModel>>> = asStateFlow(
        domain = { repository.fetchTopAiringAnime() },
        mapper = { data -> topAiringUiMapper.mapToAiringAnimeUiModel(data) },
        scope = viewModelScope
    )

    val topRanking: StateFlow<UiState<List<TopRankingUiModel>>> = asStateFlow(
        domain = { repository.fetchTopRankingAnime() },
        mapper = { data -> topRankingUiMapper.mapToTopRankingAnimeUiModel(data) },
        scope = viewModelScope
    )

}
