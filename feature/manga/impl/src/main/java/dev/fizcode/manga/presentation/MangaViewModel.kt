package dev.fizcode.manga.presentation

import dev.fizcode.common.base.mvi.MviViewModel
import dev.fizcode.common.base.presentationhandler.loadAsUiState
import dev.fizcode.common.settings.NsfwFilterProvider
import dev.fizcode.common.settings.changes
import dev.fizcode.manga.domain.usecase.FetchMangaRankingUseCase
import dev.fizcode.manga.presentation.mapper.MangaUiMapper
import dev.fizcode.manga.presentation.model.MangaIntent
import dev.fizcode.manga.presentation.model.MangaUiState
import dev.fizcode.manga.presentation.model.RankedManga
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart

internal class MangaViewModel(
    private val fetchMangaRankingUseCase: FetchMangaRankingUseCase,
    private val mangaUiMapper: MangaUiMapper,
    nsfwFilterProvider: NsfwFilterProvider
) : MviViewModel<MangaUiState, MangaIntent, Nothing>(MangaUiState()) {

    private val retries = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        combine(
            uiState.map { it.request }.distinctUntilChanged(),
            merge(retries, nsfwFilterProvider.changes()).onStart { emit(Unit) }
        ) { request, _ -> request }
            .flatMapLatest { request ->
                loadAsUiState(mangaUiMapper::mapToMangaUiModel) {
                    fetchMangaRankingUseCase(request.rankingType, request.limit)
                }.map { state -> RankedManga(request, state) }
            }
            .collectIntoState { copy(ranked = it) }
    }

    override fun onIntent(intent: MangaIntent) {
        when (intent) {
            is MangaIntent.SelectType -> setState { copy(type = intent.type) }
            is MangaIntent.SelectSort -> setState { copy(sort = intent.sort) }
            is MangaIntent.QueryChanged -> setState { copy(query = intent.query.trim()) }
            MangaIntent.Retry -> retries.tryEmit(Unit)
        }
    }
}
