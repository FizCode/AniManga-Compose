package dev.fizcode.seasonal.presentation

import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.common.base.presentationhandler.loadAsUiState
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.changes
import dev.fizcode.mal.util.SeasonHelper
import dev.fizcode.seasonal.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.seasonal.presentation.mapper.SeasonalUiMapper
import dev.fizcode.seasonal.presentation.model.SeasonalIntent
import dev.fizcode.seasonal.presentation.model.SeasonalUiState
import dev.fizcode.seasonal.util.Constant
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart

internal class SeasonalViewModel(
    private val fetchSeasonalAnimeUseCase: FetchSeasonalAnimeUseCase,
    private val seasonalUiMapper: SeasonalUiMapper,
    seasonHelper: SeasonHelper,
    nsfwFilterProvider: NsfwFilterProvider
) : MviViewModel<SeasonalUiState, SeasonalIntent, Nothing>(initialState(seasonHelper)) {

    private val retries = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        combine(
            uiState.map { it.season to it.year }.distinctUntilChanged(),
            merge(retries, nsfwFilterProvider.changes()).onStart { emit(Unit) }
        ) { selection, _ -> selection }
            .flatMapLatest { (season, year) ->
                loadAsUiState(seasonalUiMapper::mapToSeasonalUiModel) {
                    fetchSeasonalAnimeUseCase(
                        year = year,
                        season = season.apiValue,
                        limit = Constant.RESULT_LIMIT
                    )
                }
            }
            .collectIntoState { copy(seasonal = it) }
    }

    override fun onIntent(intent: SeasonalIntent) {
        when (intent) {
            is SeasonalIntent.SelectSeason -> setState { copy(season = intent.season) }
            is SeasonalIntent.SelectYear -> setState { copy(year = intent.year) }
            is SeasonalIntent.QueryChanged -> setState { copy(query = intent.query.trim()) }
            SeasonalIntent.Retry -> retries.tryEmit(Unit)
        }
    }
}

private fun initialState(seasonHelper: SeasonHelper) = SeasonalUiState(
    season = seasonHelper.getCurrentSeason(),
    year = seasonHelper.getCurrentYear(),
    years = (seasonHelper.getCurrentYear() + Constant.UPCOMING_YEARS downTo Constant.FIRST_YEAR)
        .toImmutableList()
)
