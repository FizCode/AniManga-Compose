package dev.fizcode.seasonal.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.toUiState
import dev.fizcode.common.util.SeasonHelper
import dev.fizcode.seasonal.domain.usecase.FetchSeasonalAnimeUseCase
import dev.fizcode.seasonal.presentation.mapper.SeasonalUiMapper
import dev.fizcode.seasonal.presentation.model.Season
import dev.fizcode.seasonal.presentation.model.SeasonalUiModel
import dev.fizcode.seasonal.util.Constant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

internal class SeasonalViewModel(
    private val fetchSeasonalAnimeUseCase: FetchSeasonalAnimeUseCase,
    private val seasonalUiMapper: SeasonalUiMapper,
    seasonHelper: SeasonHelper
) : ViewModel() {

    /** Local title filter applied on top of the loaded season. */
    val queryState = TextFieldState()

    /** Starts on the season we are in right now. */
    private val _selectedSeason = MutableStateFlow(Season.fromApiValue(seasonHelper.getCurrentSeason()))
    val selectedSeason: StateFlow<Season> = _selectedSeason.asStateFlow()

    private val _selectedYear = MutableStateFlow(seasonHelper.getCurrentYear())
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    /** Years offered by the filter, newest first, including [Constant.UPCOMING_YEARS] ahead. */
    val availableYears: ImmutableList<Int> =
        (seasonHelper.getCurrentYear() + Constant.UPCOMING_YEARS downTo Constant.FIRST_YEAR)
            .toImmutableList()

    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    fun selectSeason(season: Season) {
        _selectedSeason.value = season
    }

    fun selectYear(year: Int) {
        _selectedYear.value = year
    }

    fun retry() {
        retryTrigger.tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val seasonalAnime: StateFlow<UiState<List<SeasonalUiModel>>> =
        combine(
            _selectedSeason,
            _selectedYear,
            retryTrigger.onStart { emit(Unit) }
        ) { season, year, _ -> season to year }
            .flatMapLatest { (season, year) ->
                flow {
                    emit(UiState.Loading)

                    val result = try {
                        fetchSeasonalAnimeUseCase(
                            year = year,
                            season = season.apiValue,
                            limit = Constant.RESULT_LIMIT
                        )
                    } catch (e: Exception) {
                        DomainNetworkState.ErrorNetwork(exception = e, message = e.message)
                    }

                    emit(result.toUiState(seasonalUiMapper::mapToSeasonalUiModel))
                }.flowOn(Dispatchers.IO)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(Constant.STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )

    /** The loaded season narrowed by the title typed in [queryState]. */
    val seasonalResult: StateFlow<UiState<List<SeasonalUiModel>>> =
        combine(
            seasonalAnime,
            snapshotFlow { queryState.text.toString().trim() }.distinctUntilChanged()
        ) { state, query ->
            if (state is UiState.Success && query.isNotEmpty()) {
                state.data.filter { it.title.contains(query, ignoreCase = true) }
                    .let { if (it.isEmpty()) UiState.Empty else UiState.Success(it) }
            } else {
                state
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Constant.STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )
}
