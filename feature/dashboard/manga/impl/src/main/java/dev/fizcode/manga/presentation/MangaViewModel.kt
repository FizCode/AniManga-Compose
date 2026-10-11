package dev.fizcode.manga.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import dev.fizcode.common.base.presentationhandler.toUiState
import dev.fizcode.manga.domain.usecase.FetchMangaRankingUseCase
import dev.fizcode.manga.presentation.mapper.MangaUiMapper
import dev.fizcode.manga.presentation.model.MangaSort
import dev.fizcode.manga.presentation.model.MangaType
import dev.fizcode.manga.presentation.model.MangaUiModel
import dev.fizcode.manga.util.Constant
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

internal class MangaViewModel(
    private val fetchMangaRankingUseCase: FetchMangaRankingUseCase,
    private val mangaUiMapper: MangaUiMapper
) : ViewModel() {

    /** Local title filter applied on top of the loaded list. */
    val queryState = TextFieldState()

    private val _selectedType = MutableStateFlow(MangaType.ALL)
    val selectedType: StateFlow<MangaType> = _selectedType.asStateFlow()

    private val _selectedSort = MutableStateFlow(MangaSort.RANKING)
    val selectedSort: StateFlow<MangaSort> = _selectedSort.asStateFlow()

    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    fun selectType(type: MangaType) {
        _selectedType.value = type
    }

    fun selectSort(sort: MangaSort) {
        _selectedSort.value = sort
    }

    fun retry() {
        retryTrigger.tryEmit(Unit)
    }

    /** What to ask MAL for. Popularity and favorites are one list each, so the tab only filters it. */
    private data class RankingRequest(val rankingType: String, val limit: Int)

    private fun resolveRequest(type: MangaType, sort: MangaSort) = RankingRequest(
        rankingType = sort.rankingType ?: type.rankingType,
        limit = if (sort.rankingType == null) type.limit else Constant.RANKING_LIMIT
    )

    /** A loaded list together with the request it answers, so a stale list is never shown for a new tab. */
    private data class Ranked(val request: RankingRequest?, val state: UiState<List<MangaUiModel>>)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ranking: StateFlow<Ranked> =
        combine(
            _selectedType,
            _selectedSort,
            retryTrigger.onStart { emit(Unit) }
        ) { type, sort, _ -> resolveRequest(type, sort) }
            .distinctUntilChanged()
            .flatMapLatest { request ->
                flow {
                    emit(Ranked(request, UiState.Loading))

                    val result = try {
                        fetchMangaRankingUseCase(request.rankingType, request.limit)
                    } catch (e: Exception) {
                        DomainNetworkState.ErrorNetwork(exception = e, message = e.message)
                    }

                    emit(Ranked(request, result.toUiState(mangaUiMapper::mapToMangaUiModel)))
                }.flowOn(Dispatchers.IO)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(Constant.STOP_TIMEOUT_MILLIS),
                initialValue = Ranked(request = null, state = UiState.Loading)
            )

    /** The loaded list narrowed by the selected type and the title typed in [queryState]. */
    val mangaResult: StateFlow<UiState<List<MangaUiModel>>> =
        combine(
            ranking,
            _selectedType,
            _selectedSort,
            snapshotFlow { queryState.text.toString().trim() }.distinctUntilChanged()
        ) { ranked, type, sort, query ->
            val state = ranked.state
            if (ranked.request != resolveRequest(type, sort)) {
                UiState.Loading
            } else if (state is UiState.Success) {
                state.data
                    .filter { type.matches(it.mediaType) && it.title.contains(query, ignoreCase = true) }
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
