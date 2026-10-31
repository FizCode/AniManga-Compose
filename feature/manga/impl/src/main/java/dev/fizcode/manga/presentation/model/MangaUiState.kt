package dev.fizcode.manga.presentation.model

import dev.fizcode.common.base.callhandler.UiState

internal data class MangaUiState(
    val type: MangaType = MangaType.ALL,
    val sort: MangaSort = MangaSort.RANKING,
    val query: String = "",
    val ranked: RankedManga = RankedManga()
) {
    val request: MangaRankingRequest = MangaRankingRequest.of(type, sort)

    val result: UiState<List<MangaUiModel>> = when {
        ranked.request != request -> UiState.Loading
        ranked.state is UiState.Success -> ranked.state.data
            .filter { type.matches(it.mediaType) && it.titles.matches(query) }
            .let { if (it.isEmpty()) UiState.Empty else UiState.Success(it) }
        else -> ranked.state
    }
}
