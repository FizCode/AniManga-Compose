package dev.fizcode.ranking.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.asUiStateFlow
import dev.fizcode.ranking.domain.usecase.FetchAnimeRankingUseCase
import dev.fizcode.ranking.presentation.mapper.RankingUiMapper
import dev.fizcode.ranking.presentation.model.RankingType
import dev.fizcode.ranking.presentation.model.RankingUiModel
import dev.fizcode.ranking.util.Constant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.changes
import kotlinx.coroutines.flow.merge

internal class RankingViewModel(
    /** The MAL `ranking_type` to list, see [RankingType.apiValue]. */
    private val rankingType: String,
    private val fetchAnimeRankingUseCase: FetchAnimeRankingUseCase,
    private val rankingUiMapper: RankingUiMapper,
    nsfwFilterProvider: NsfwFilterProvider
) : ViewModel() {

    /** Local title filter applied on top of the loaded ranking. */
    val queryState = TextFieldState()

    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    fun retry() {
        retryTrigger.tryEmit(Unit)
    }

    private val ranking: StateFlow<UiState<List<RankingUiModel>>> = asUiStateFlow(
        scope = viewModelScope,
        domain = { fetchAnimeRankingUseCase(rankingType, Constant.RESULT_LIMIT) },
        mapper = rankingUiMapper::mapToRankingUiModel,
        retrigger = merge(retryTrigger, nsfwFilterProvider.changes()),
        started = SharingStarted.WhileSubscribed(Constant.STOP_TIMEOUT_MILLIS)
    )

    /** The loaded ranking narrowed by the title typed in [queryState], ranks are kept. */
    val rankingResult: StateFlow<UiState<List<RankingUiModel>>> = combine(
        ranking,
        snapshotFlow { queryState.text.toString().trim() }.distinctUntilChanged()
    ) { state, query ->
        if (state is UiState.Success && query.isNotEmpty()) {
            state.data.filter { it.titles.matches(query) }
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
