package dev.fizcode.search.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.toUiState
import dev.fizcode.search.domain.usecase.SearchAnimeUseCase
import dev.fizcode.search.presentation.mapper.SearchUiMapper
import dev.fizcode.search.presentation.model.SearchResultUiModel
import dev.fizcode.search.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.changes
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

internal class SearchViewModel(
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val searchUiMapper: SearchUiMapper,
    nsfwFilterProvider: NsfwFilterProvider
) : ViewModel() {

    val queryState = TextFieldState()

    /**
     * Emits [UiState.Loading] as soon as the query is long enough, then waits
     * [Constant.DEBOUNCE_MILLIS] before calling the API. A newer query cancels the pending one.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResult: StateFlow<UiState<List<SearchResultUiModel>>> =
        // A new NSFW level searches the same query again.
        combine(
            snapshotFlow { queryState.text.toString().trim() }.distinctUntilChanged(),
            nsfwFilterProvider.changes().onStart { emit(Unit) }
        ) { query, _ -> query }
        .flatMapLatest { query ->
            if (query.length < Constant.MIN_QUERY_LENGTH) {
                flowOf(UiState.Empty)
            } else {
                flow {
                    emit(UiState.Loading)
                    delay(Constant.DEBOUNCE_MILLIS)

                    val result = try {
                        searchAnimeUseCase(query = query, limit = Constant.RESULT_LIMIT)
                    } catch (e: Exception) {
                        DomainNetworkState.ErrorNetwork(exception = e, message = e.message)
                    }

                    emit(result.toUiState(searchUiMapper::mapToSearchResultUiModel))
                }.flowOn(Dispatchers.IO)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Empty
        )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
