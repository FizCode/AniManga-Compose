package dev.fizcode.ranking.presentation

import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.common.base.presentationhandler.loadAsUiState
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.changes
import dev.fizcode.ranking.domain.usecase.FetchAnimeRankingUseCase
import dev.fizcode.ranking.presentation.mapper.RankingUiMapper
import dev.fizcode.ranking.presentation.model.RankingIntent
import dev.fizcode.ranking.presentation.model.RankingUiState
import dev.fizcode.ranking.util.Constant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart

internal class RankingViewModel(
    private val rankingType: String,
    private val fetchAnimeRankingUseCase: FetchAnimeRankingUseCase,
    private val rankingUiMapper: RankingUiMapper,
    nsfwFilterProvider: NsfwFilterProvider
) : MviViewModel<RankingUiState, RankingIntent, Nothing>(RankingUiState()) {

    private val retries = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        merge(retries, nsfwFilterProvider.changes())
            .onStart { emit(Unit) }
            .flatMapLatest {
                loadAsUiState(rankingUiMapper::mapToRankingUiModel) {
                    fetchAnimeRankingUseCase(rankingType, Constant.RESULT_LIMIT)
                }
            }
            .collectIntoState { copy(ranking = it) }
    }

    override fun onIntent(intent: RankingIntent) {
        when (intent) {
            is RankingIntent.QueryChanged -> setState { copy(query = intent.query.trim()) }
            RankingIntent.Retry -> retries.tryEmit(Unit)
        }
    }
}
