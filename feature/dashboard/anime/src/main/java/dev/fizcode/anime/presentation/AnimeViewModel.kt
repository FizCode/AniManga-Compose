package dev.fizcode.anime.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class AnimeViewModel(
    private val animeUseCaseGroup: AnimeUseCaseGroup,
    private val seasonalUiMapper: SeasonalAnimeUiMapper,
    private val topAiringUiMapper: TopAiringAnimeUiMapper,
    private val topRankingUiMapper: TopRankingAnimeUiMapper
) : ViewModel() {

    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 0)
    private var _refreshing = MutableStateFlow(false)
    val refreshing: MutableStateFlow<Boolean> = _refreshing
    fun refresh() = viewModelScope.launch {
        _refreshing.emit(true)
        _refreshTrigger.emit(Unit)
    }

    fun shouldNotRefresh() = viewModelScope.launch {
        _refreshing.emit(false)
    }

    val currentSeason: StateFlow<UiState<List<SeasonalUiModel>>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeUseCaseGroup.fetchSeasonAnimeUseCase(Constant.LIMIT_5) },
        mapper = { data -> seasonalUiMapper.mapToSeasonalAnimeUiModel(data) },
        retrigger = _refreshTrigger,
        started = SharingStarted.Lazily
    )

    val topAiring: StateFlow<UiState<List<TopAiringUiModel>>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeUseCaseGroup.fetchTopAiringAnimeUseCase(Constant.LIMIT_5) },
        mapper = { data -> topAiringUiMapper.mapToAiringAnimeUiModel(data) },
        retrigger = _refreshTrigger,
        started = SharingStarted.Lazily
    )

    val topRanking: StateFlow<UiState<List<TopRankingUiModel>>> = asStateFlow(
        scope = viewModelScope,
        domain = { animeUseCaseGroup.fetchTopRankingAnimeUseCase(Constant.LIMIT_10) },
        mapper = { data -> topRankingUiMapper.mapToTopRankingAnimeUiModel(data) },
        retrigger = _refreshTrigger,
        started = SharingStarted.Lazily
    )

}
