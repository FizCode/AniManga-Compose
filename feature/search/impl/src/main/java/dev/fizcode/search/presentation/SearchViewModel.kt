package dev.fizcode.search.presentation

import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.common.base.presentationhandler.loadAsUiState
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.changes
import dev.fizcode.search.domain.usecase.SearchAnimeUseCase
import dev.fizcode.search.presentation.mapper.SearchUiMapper
import dev.fizcode.search.presentation.model.SearchIntent
import dev.fizcode.search.presentation.model.SearchUiState
import dev.fizcode.search.util.Constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal class SearchViewModel(
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val searchUiMapper: SearchUiMapper,
    nsfwFilterProvider: NsfwFilterProvider
) : MviViewModel<SearchUiState, SearchIntent, Nothing>(SearchUiState()) {

    init {
        combine(
            uiState.map { it.query }.distinctUntilChanged(),
            nsfwFilterProvider.changes().onStart { emit(Unit) }
        ) { query, _ -> query }
            .flatMapLatest { query ->
                if (query.length < Constant.MIN_QUERY_LENGTH) {
                    flowOf(UiState.Empty)
                } else {
                    loadAsUiState(searchUiMapper::mapToSearchResultUiModel) {
                        delay(Constant.DEBOUNCE_MILLIS)
                        searchAnimeUseCase(query = query, limit = Constant.RESULT_LIMIT)
                    }
                }
            }
            .collectIntoState { copy(result = it) }
    }

    override fun onIntent(intent: SearchIntent) = when (intent) {
        is SearchIntent.QueryChanged -> setState { copy(query = intent.query.trim()) }
    }
}
